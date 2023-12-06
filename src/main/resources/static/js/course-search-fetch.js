const search_button = document.getElementById("course-search-button");
search_button.addEventListener("click", () => {
    const keyword_input = document.getElementById("keyword_input").value;
    let coursesTag = document.getElementById("course-table-body");
    coursesTag.innerHTML = '';
    fetch('/api/course?keyword=' + keyword_input, {
        headers: {
            'Content-Type': 'application/json'
        },
        method: "GET"
    })
        .then(response => response.json())
        .then(list => {
            list.forEach(courseOverviewDto => {
                const courseId = courseOverviewDto.id;
                const courseTag = document.createElement('tr');

                const courseNameTag = document.createElement('th');
                courseNameTag.textContent = courseOverviewDto.name;
                courseTag.appendChild(courseNameTag);

                const courseDescTag = document.createElement('th');
                courseDescTag.textContent = courseOverviewDto.description;
                courseTag.appendChild(courseDescTag);

                const courseCreatedAtTag = document.createElement('th');
                courseCreatedAtTag.id = "createdAtTh";
                const rawDate = new Date(courseOverviewDto.createdAt);
                const formattedDate = `${rawDate.getFullYear()}-${(rawDate.getMonth() + 1).toString().padStart(2, '0')}-${rawDate.getDate().toString().padStart(2, '0')}`;

                courseCreatedAtTag.textContent = formattedDate;
                courseTag.appendChild(courseCreatedAtTag);

                // 팀 가입 api
                const joinButtonTag = document.createElement('th');
                const courseJoinButton = document.createElement('button');
                courseJoinButton.type = "button";
                courseJoinButton.id = "join-button";
                courseJoinButton.name = "가입하기";
                courseJoinButton.setAttribute('class', "btn btn-success");
                courseJoinButton.addEventListener('click', () => {
                    const joinCode = prompt('해당 팀의 참여코드를 입력하세요 :');
                    fetch('/api/course/' + courseId + '/member', {
                        headers: {
                            "Authorization": auth,
                            'Content-Type': 'application/json'
                        },
                        method: "POST",
                        body: JSON.stringify({
                            "joinCode": joinCode
                        })
                    })
                        .then(response => {
                            if (response.ok) window.location.href = '/views/course/' + courseId;
                            else {
                                response.json().then(error => {
                                    alert(error.message);
                                })
                            }
                        })
                })

                // 추가하기
                joinButtonTag.appendChild(courseJoinButton);
                courseTag.appendChild(joinButtonTag);
                coursesTag.appendChild(courseTag);
            })
        })
})