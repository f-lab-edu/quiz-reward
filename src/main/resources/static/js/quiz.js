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
        $('#question').empty();
        $('#question').text(data.question);
        // 또는 콘솔 확인
        console.log('퀴즈:', data);
    }
}