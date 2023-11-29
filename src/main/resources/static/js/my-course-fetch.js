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
                course_name_cell.href = '/views/course/' + course_id;
                course_name_cell.textContent = course.name;
                course_table.appendChild(course_name_cell);

                // role
                const role_cell = document.createElement('tr');
                role_cell.textContent = course.role;
                course_table.appendChild(role_cell);

                if (course.role === "Student") {
                    // progress
                    const progress_cell = document.createElement('tr');
                    progress_cell.textContent = course.progress;
                    course_table.appendChild(progress_cell);
                }
                wrapper.appendChild(course_table);
            })
        }
    })