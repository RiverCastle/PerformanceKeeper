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
                course_link.href = '/views/course/' + course_id;
                course_link.textContent = course.name;
                course_name_cell.appendChild(course_link);
                course_table.appendChild(course_name_cell);

                const course_progress_cell = document.createElement('tr');
                course_progress_cell.textContent = '학생들의 과제 완수율'
                // TODO 부여된 과제들의 완수율을 전달 받아서 출력하기
                course_table.appendChild(course_progress_cell);

                wrapper.appendChild(course_table);
            })
        }
    })