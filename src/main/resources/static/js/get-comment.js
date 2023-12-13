fetch('/api/course/' + course_id + '/assignedTask/' + assigned_task_id + '/comment', {
    headers: {
        'Authorization': auth
    },
    method: "GET"
})
    .then(response => {
        if (response.ok) response.json().then(data => {
            const comments_div = document.getElementById('comments-container');

            data.forEach(comment => {
                const comment_id = comment.id;
                const data_created_at = new Date(comment.createdAt);
                const comment_created_at = data_created_at.getMonth() + 1 + "/" + data_created_at.getDate() + "  " + data_created_at.getHours() + ":" + data_created_at.getMinutes();
                const writerName = comment.writerName;
                const content = comment.content;

                const commentElement = document.createElement('ul');
                commentElement.innerHTML = `<p>${comment_created_at} | <strong>${writerName}:</strong> ${content}</p>`;

                // 댓글삭제버튼
                if (member_id === comment.writerId) {
                    const comment_delete_button = document.createElement('button');
                    comment_delete_button.textContent = "댓글삭제";
                    comment_delete_button.className = "comment_delete_button"
                    comment_delete_button.addEventListener('click', () => {
                        alert("정말로 댓글을 삭제하시겠습니까?");
                        fetch('/api/course/' + course_id + '/assignedTask/' + assigned_task_id + '/comment/' + comment_id, {
                            headers: {
                                "Authorization": auth
                            },
                            method: "DELETE"
                        })
                            .then(response => {
                                if (response.ok) {
                                    alert("댓글이 삭제되었습니다.");
                                    window.location.reload();
                                }

                            })
                            .catch(error => {
                                alert(error.message);
                            })
                    })
                    commentElement.appendChild(comment_delete_button);
                }

                //답글
                if (comment.replies) {
                    comment.replies.forEach(reply => {
                        const replyId = reply.id;
                        const data_created_at = new Date(reply.createdAt);
                        const reply_created_at = data_created_at.getMonth() + 1 + "/" + data_created_at.getDate() + "  " + data_created_at.getHours() + ":" + data_created_at.getMinutes();
                        const replyElement = document.createElement('ul');
                        replyElement.innerHTML = `<p>└ ${reply_created_at} | <strong>${reply.writerName}:</strong> ${reply.content}</p>`;

                        // 답글 삭제 버튼
                        if (member_id === reply.writerId) {
                            const reply_delete_button = document.createElement('button');
                            reply_delete_button.textContent = "답글삭제";
                            reply_delete_button.className = "reply_delete_button"
                            reply_delete_button.addEventListener('click', () => {
                                alert("정말로 답글을 삭제하시겠습니까?");
                                fetch('/api/course/' + course_id + '/assignedTask/' + assigned_task_id + '/comment/' + comment_id + '/reply/' + replyId, {
                                    headers: {
                                        "Authorization": auth
                                    },
                                    method: "DELETE"
                                })
                                    .then(response => {
                                        if (response.ok) {
                                            alert("답글이 삭제되었습니다.");
                                            window.location.reload();
                                        }
                                    })
                                    .catch(error => {
                                        alert(error.message);
                                    })
                            })
                            replyElement.appendChild(reply_delete_button);

                        }

                        // 답글을 댓글 아래에 표시하거나 원하는 위치에 추가
                        commentElement.appendChild(replyElement);
                    });
                }

                const reply_add_button = document.createElement('button');
                reply_add_button.className = "new_reply_button"
                reply_add_button.textContent = "답글달기";
                reply_add_button.addEventListener('click', () => {
                    const new_reply_content = prompt("답글을 입력해주세요.");
                    if (new_reply_content !== null) {
                        const commentId = comment.id;
                        // 답글 추가 api
                        fetch('/api/course/' + course_id + '/assignedTask/' + assigned_task_id + '/comment/' + commentId + '/reply', {
                            headers: {
                                "Authorization": auth,
                                "Content-Type": "application/json"
                            },
                            method: "POST",
                            body: JSON.stringify({
                                "content": new_reply_content
                            })
                        })
                            .then(response => {
                                window.location.reload()
                            })
                            .catch(error => {
                                alert(error.method);
                                window.location.href = "/views/login";
                            });
                    }
                })
                commentElement.appendChild(reply_add_button);
                // 표시할 요소에 추가하거나 DOM에 직접 추가
                commentElement.appendChild(document.createElement('hr'))
                comments_div.appendChild(commentElement);
            });
        })
    })