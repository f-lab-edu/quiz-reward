$(document).ready(function () {
    initEvents();
});

function initEvents(){
    $('#submitAnswer').on('click', ()=>{

    });

    $('#startQuiz').on('click', async ()=>{
        document.getElementById('quizSection').style.display = 'block';
        window.scrollTo({ top: document.getElementById('quizSection').offsetTop - 20, behavior: 'smooth' });
        await fetchCurrentQuiz();
    });

}

async function fetchCurrentQuiz() {
    const res = await fetch('/api/quiz/current');
    const data = await res.json();

    if (data.message) {
        alert(data.message);
    } else {
        const startRes = await fetch(`/api/quiz/start?quizId=${data.id}`);
        if (!startRes.ok) {
            alert('이미 퀴즈에 참여하셨습니다.');
            return;
        }
        $('#question').empty();
        $('#question').text(data.question);
        console.log('퀴즈:', data);
    }
}