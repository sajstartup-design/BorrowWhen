function updatePagination(pagination){
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');

    if(pagination){

        let hasNext = pagination.hasNext;
        let hasPrevious = pagination.hasPrevious;
        let currPage = pagination.page;

        // Fix: use 'disabled' instead of 'disable'
        nextBtn.disabled = !hasNext;
        prevBtn.disabled = !hasPrevious;

        inputPage.value = currPage;
    }
}
