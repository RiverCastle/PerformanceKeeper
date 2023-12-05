const course_name_tag = document.getElementById('course-name-tag');
const course_desc_tag = document.getElementById('course-desc-tag');


fetch('/api/course/' + course_id, {
    headers: {
        'Authorization': auth
    },
    method: "GET"
})
    .then(response => {
        if (response.ok) response.json().then(data => {
            console.log(data)
            course_name_tag.innerText = data.name
            course_desc_tag.innerText = data.description
        })
        else response.json().then(error => alert(error.message));
    })