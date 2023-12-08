const nickname_change_button = document.getElementById('nickname-change-button');
nickname_change_button.addEventListener('click', () => {
    const nickname_input = prompt("새 닉네임을 입력해주세요.");
    if (nickname_input !== null) {
        fetch('/api/course/' + course_id + '/member', {
            headers: {
                'Authorization' : auth,
                'Content-Type' : 'application/json'
            },
            method: "PUT",
            body: JSON.stringify({
                'nickname' : nickname_input
            })
        })
            .then(response => {
                if (response.ok) alert('닉네임이 ' + nickname_input + '으로 변경되었습니다.')
                else alert('닉네임 변경에 실패했습니다.')
            })
    }


})