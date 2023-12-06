fetch('/api/course/myCourse', {
    headers: {
        'Authorization': auth,
    },
    method: "GET"
})
    .then(response => response.json())
    .then(OverviewDtoList => {

        const wrapper = document.getElementById("my-courses-wrapper");
        if (OverviewDtoList.size === 0) {

        } else {
            OverviewDtoList.forEach(course => {
                console.log(course)
                const course_id = course.id;
                const course_table = document.createElement('table');

                // course name
                const course_name_cell = document.createElement('tr');
                const course_link = document.createElement('a');
                course_link.href = '/views/manager/course/' + course_id;
                course_link.textContent = course.name;
                course_link.setAttribute('class', "d-inline-block text-truncate");
                course_name_cell.appendChild(course_link);
                course_table.appendChild(course_name_cell);

                const course_progress_cell = document.createElement('tr');
                course_progress_cell.textContent = course.progress + '%';
                course_table.appendChild(course_progress_cell);

                wrapper.appendChild(course_table);
            })
        }
    })