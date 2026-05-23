const subjectsCache = new Map();

function getApiBase() {
    const meta = document.querySelector('meta[name="api-base"]');
    if (meta && meta.content) {
        return meta.content;
    }
    if (window.API_BASE) {
        return window.API_BASE;
    }
    return window.location.pathname.startsWith("/lecturer")
        ? "/api/lecturer/subjects"
        : "/api/admin/subjects";
}

async function apiFetch(url, options = {}) {
    const response = await fetch(url, {
        credentials: "include",
        headers: { "Content-Type": "application/json", ...(options.headers || {}) },
        ...options
    });

    const contentType = response.headers.get("content-type") || "";

    if (response.status === 401 || response.status === 403) {
        alert("Phiên đăng nhập hết hạn hoặc không đủ quyền. Vui lòng đăng nhập lại.");
        window.location.href = "/login";
        throw new Error("Unauthorized");
    }

    if (!contentType.includes("application/json")) {
        const text = await response.text();
        throw new Error(text && text.length < 200 ? text : "Máy chủ trả về dữ liệu không hợp lệ (HTTP " + response.status + ")");
    }

    return response;
}

document.addEventListener("DOMContentLoaded", () => {
    const modalEl = document.getElementById("subjectModal");
    if (modalEl) {
        modalEl.addEventListener("show.bs.modal", (event) => {
            const trigger = event.relatedTarget;
            if (trigger && trigger.dataset.mode === "create") {
                resetCreateForm();
            }
        });
    }

    const saveBtn = document.getElementById("btnSaveSubject");
    if (saveBtn) {
        saveBtn.addEventListener("click", saveSubject);
    }

    loadSubjects();
});

function getModal() {
    const el = document.getElementById("subjectModal");
    if (!el) {
        alert("Không tìm thấy form môn học. Vui lòng tải lại trang.");
        return null;
    }
    if (typeof bootstrap === "undefined") {
        alert("Bootstrap chưa tải. Vui lòng tải lại trang (Ctrl+F5).");
        return null;
    }
    return bootstrap.Modal.getOrCreateInstance(el);
}

function resetCreateForm() {
    document.getElementById("subjectModalTitle").textContent = "Thêm môn học";
    document.getElementById("subjectId").value = "";
    document.getElementById("code").value = "";
    document.getElementById("name").value = "";
    document.getElementById("description").value = "";
    document.getElementById("status").value = "ACTIVE";
    document.getElementById("code").disabled = false;
}

async function loadSubjects() {
    const tbody = document.getElementById("subjectTableBody");
    if (!tbody) return;

    tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted">Đang tải...</td></tr>`;

    try {
        const response = await apiFetch(getApiBase());
        if (!response.ok) {
            const err = await response.json();
            throw new Error(err.message || "HTTP " + response.status);
        }
        const data = await response.json();
        renderTable(data);
    } catch (err) {
        console.error("loadSubjects:", err);
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">
            Không tải được dữ liệu: ${escapeHtml(err.message)}
        </td></tr>`;
    }
}

function renderTable(subjects) {
    const tbody = document.getElementById("subjectTableBody");
    subjectsCache.clear();
    subjects.forEach(s => subjectsCache.set(s.id, s));

    if (!subjects.length) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted">Chưa có môn học nào. Bấm "Thêm môn học" để tạo.</td></tr>`;
        return;
    }

    tbody.innerHTML = subjects.map(subject => {
        const statusBadge = subject.status === "ACTIVE"
            ? `<span class="badge bg-success">ACTIVE</span>`
            : `<span class="badge bg-secondary">HIDDEN</span>`;

        const createdAt = subject.createdAt
            ? new Date(subject.createdAt).toLocaleString("vi-VN")
            : "-";

        const desc = subject.description
            ? escapeHtml(subject.description).substring(0, 80) + (subject.description.length > 80 ? "..." : "")
            : "-";

        const publishBtn = subject.status === "ACTIVE"
            ? `<button type="button" class="btn btn-sm btn-outline-secondary" onclick="togglePublish(${subject.id}, false)">Unpublish</button>`
            : `<button type="button" class="btn btn-sm btn-outline-success" onclick="togglePublish(${subject.id}, true)">Publish</button>`;

        return `
            <tr>
                <td>${subject.id}</td>
                <td><strong>${escapeHtml(subject.code)}</strong></td>
                <td>${escapeHtml(subject.name)}</td>
                <td>${desc}</td>
                <td>${statusBadge}</td>
                <td>${createdAt}</td>
                <td class="text-end">
                    <div class="btn-group btn-group-sm">
                        <button type="button" class="btn btn-outline-primary" onclick="openEditModal(${subject.id})">Sửa</button>
                        ${publishBtn}
                        <button type="button" class="btn btn-outline-danger" onclick="deleteSubject(${subject.id})">Xóa</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

function openEditModal(id) {
    const subject = subjectsCache.get(id);
    if (!subject) return;

    document.getElementById("subjectModalTitle").textContent = "Sửa môn học";
    document.getElementById("subjectId").value = subject.id;
    document.getElementById("code").value = subject.code;
    document.getElementById("name").value = subject.name;
    document.getElementById("description").value = subject.description || "";
    document.getElementById("status").value = subject.status === "INACTIVE" ? "HIDDEN" : subject.status;
    document.getElementById("code").disabled = false;

    const modal = getModal();
    if (modal) modal.show();
}

async function saveSubject() {
    const saveBtn = document.getElementById("btnSaveSubject");
    if (saveBtn) saveBtn.disabled = true;

    try {
        const id = document.getElementById("subjectId").value;
        const description = document.getElementById("description").value.trim();

        const payload = {
            code: document.getElementById("code").value.trim(),
            name: document.getElementById("name").value.trim(),
            description: description || null,
            status: document.getElementById("status").value
        };

        if (!payload.code || !payload.name) {
            alert("Vui lòng nhập mã môn và tên môn.");
            return;
        }

        const apiBase = getApiBase();
        const url = id ? `${apiBase}/${id}` : apiBase;
        const method = id ? "PUT" : "POST";

        const response = await apiFetch(url, {
            method,
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const err = await response.json();
            alert(err.message || "Lưu thất bại (HTTP " + response.status + ")");
            return;
        }

        const modal = getModal();
        if (modal) modal.hide();

        await loadSubjects();
        alert(id ? "Đã cập nhật môn học!" : "Đã thêm môn học mới!");
    } catch (err) {
        console.error("saveSubject:", err);
        if (err.message !== "Unauthorized") {
            alert("Lưu thất bại: " + err.message);
        }
    } finally {
        if (saveBtn) saveBtn.disabled = false;
    }
}

async function togglePublish(id, publish) {
    const action = publish ? "publish" : "unpublish";
    try {
        const response = await apiFetch(`${getApiBase()}/${id}/${action}`, { method: "PATCH" });
        if (!response.ok) {
            const err = await response.json();
            alert(err.message || "Không thể cập nhật trạng thái.");
            return;
        }
        await loadSubjects();
    } catch (err) {
        if (err.message !== "Unauthorized") {
            alert("Lỗi: " + err.message);
        }
    }
}

async function deleteSubject(id) {
    if (!confirm("Bạn có chắc muốn xóa môn học này?")) {
        return;
    }

    try {
        const response = await apiFetch(`${getApiBase()}/${id}`, { method: "DELETE" });
        if (!response.ok) {
            const err = await response.json();
            alert(err.message || "Không thể xóa môn học.");
            return;
        }
        await loadSubjects();
        alert("Đã xóa môn học.");
    } catch (err) {
        if (err.message !== "Unauthorized") {
            alert("Lỗi: " + err.message);
        }
    }
}

function escapeHtml(text) {
    if (!text) return "";
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
