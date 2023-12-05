const sign_up_button = document.getElementById("course-create-button");
sign_up_button.addEventListener('click', () => {
        const course_name = document.getElementById("course_name_input").value;
        const course_joincode = document.getElementById("course_joincode_input").value;
        const course_desc = document.getElementById("course_desc_input").value;

        fetch('/api/course', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : auth
                },
                method: "POST",
                body: JSON.stringify({
                    "name": course_name,
                    "joinCode": course_joincode,
                    "description": course_desc
                })
            }
        )
            .then(response => {
                if (response.ok) {
                    alert("강의실 생성이 완료되었습니다.")
                } else {
                    response.json().then(error => {
                        alert(error.message);
                        console.log(error.message);
                    })
                }
            })
    }
)