let member_id;
let member_nickname;
fetch('/api/course/' + course_id + '/member', {
    headers: {
        'Authorization' : auth
    },
    method: 'GET'
})
.then(response => {
    if (response.ok) response.json().then(member_dto => {
        member_id = member_dto.id;
        member_nickname = member_dto.nickname;
    })
})