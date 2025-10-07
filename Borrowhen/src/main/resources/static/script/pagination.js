function updatePagination(pagination) {
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');
    const info = document.getElementById('table-info');

    if (pagination) {
        const pageSize = 10; // adjust if your page size is different
        const currPage = pagination.page; // 0-based
        const totalElements = pagination.totalElements;

        // Enable/disable buttons
        nextBtn.disabled = !pagination.hasNext;
        prevBtn.disabled = !pagination.hasPrevious;

        // Update input (1-based)
        inputPage.value = currPage + 1;

        // Update info text
        const start = currPage * pageSize + 1;
        const end = Math.min(start + pageSize - 1, totalElements);
        info.textContent = `Showing ${start} to ${end} of ${totalElements} entries`;
    }
}
