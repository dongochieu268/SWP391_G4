async function loadSubjects() {

    try {

        const response = await fetch("http://localhost:8080/api/subjects");

        const data = await response.json();

        const table = document.getElementById("subjectTable");

        table.innerHTML = "";

        data.forEach(subject => {

            table.innerHTML += `
            <tr>
                <td>${subject.id}</td>
                <td>${subject.code}</td>
                <td>${subject.name}</td>
                <td>${subject.status}</td>
            </tr>
            `;

        });

    } catch (error) {

        console.error(error);

    }

}

loadSubjects();