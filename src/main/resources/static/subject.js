const API_URL = "http://localhost:8080/api/subjects";

async function loadSubjects() {

    const response = await fetch(API_URL);

    const data = await response.json();

    const table = document.getElementById("subjectTable");

    table.innerHTML = "";

    data.forEach(subject => {

        let statusClass =
            subject.status === "ACTIVE"
                ? "status-active"
                : "status-inactive";

        table.innerHTML += `
            <tr>
                <td>${subject.id}</td>
                <td>${subject.code}</td>
                <td>${subject.name}</td>

                <td class="${statusClass}">
                    ${subject.status}
                </td>

                <td>

                    <button class="edit-btn"
                        onclick="editSubject(
                            ${subject.id},
                            '${subject.code}',
                            '${subject.name}',
                            '${subject.status}'
                        )">
                        Edit
                    </button>

                    <button class="delete-btn"
                        onclick="deleteSubject(${subject.id})">
                        Delete
                    </button>

                </td>

            </tr>
        `;
    });
}

async function saveSubject() {

    const id = document.getElementById("subjectId").value;

    const subject = {

        code: document.getElementById("code").value,

        name: document.getElementById("name").value,

        status: document.getElementById("status").value
    };

    if (id) {

        await fetch(`${API_URL}/${id}`, {

            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(subject)
        });

    } else {

        await fetch(API_URL, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(subject)
        });
    }

    clearForm();

    loadSubjects();
}

function editSubject(id, code, name, status) {

    document.getElementById("subjectId").value = id;

    document.getElementById("code").value = code;

    document.getElementById("name").value = name;

    document.getElementById("status").value = status;
}

async function deleteSubject(id) {

    if (confirm("Are you sure?")) {

        await fetch(`${API_URL}/${id}`, {

            method: "DELETE"
        });

        loadSubjects();
    }
}

function clearForm() {

    document.getElementById("subjectId").value = "";

    document.getElementById("code").value = "";

    document.getElementById("name").value = "";

    document.getElementById("status").value = "ACTIVE";
}

loadSubjects();