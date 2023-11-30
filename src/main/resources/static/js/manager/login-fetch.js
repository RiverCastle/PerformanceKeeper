document.getElementById("login-fetch-button").addEventListener("click", () => {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // 로그인 API 엔드포인트로 POST 요청 보내기
    fetch('/api/token', {
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify({
            "username": username,
            "password": password
        })
    })
        .then(response => {
            if (response.ok) response.json()
                .then(accesstoken => {
                    sessionStorage.setItem("JWT", accesstoken.token)
                    window.location.href = "/views/manager/main";
                })
             else {
                response.json().then(error => {
                    alert(error.message);
                    console.log(error.message);
                })
            }
        })
});