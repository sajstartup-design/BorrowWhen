var currentPage = 1;

function updatePagination(pagination) {
  const prevBtn = document.querySelector('.prev-btn');
  const nextBtn = document.querySelector('.next-btn');
  const pageBtns = document.querySelectorAll('.page-btn');
  const endBtn = document.querySelector('.end-btn');
  const selectPage = document.querySelector('.input-page');
  const info = document.getElementById('table-info');

  if (!pagination) return;

  const totalPages = pagination.totalPages;
  const pageSize = pagination.pageSize; // adjust as needed
  const totalElements = pagination.totalElements;
  let { hasNext, hasPrevious, page } = pagination;

  // update current page (1-based)
  currentPage = page + 1;

  // enable/disable prev & next
  if (prevBtn) prevBtn.disabled = !hasPrevious;
  if (nextBtn) nextBtn.disabled = !hasNext;

  // update info
  if (info) {
    const start = (currentPage - 1) * pageSize + 1;
    const end = Math.min(start + (pagination.numberOfElements || pageSize) - 1, totalElements);
    info.textContent = `Showing ${start} to ${end} of ${totalElements} entries`;
  }


  // update select dropdown if exists
  if (selectPage) {
    selectPage.innerHTML = ''; // clear previous options
    for (let i = 1; i <= totalPages; i++) {
      const option = document.createElement('option');
      option.value = i;
      option.textContent = i;
      if (i === currentPage) option.selected = true;
      selectPage.appendChild(option);
    }
  }

  // --- fixed window calculation (simple, like working example) ---
  let startPage = Math.max(1, currentPage - 1); // show one page before
  let endPage = startPage + (pageBtns ? pageBtns.length - 1 : 0);
  if (endPage > totalPages) {
    endPage = totalPages;
    startPage = Math.max(1, endPage - (pageBtns ? pageBtns.length - 1 : 0));
  }

  // update page buttons
  if (pageBtns && pageBtns.length) {
    pageBtns.forEach((btn, idx) => {
      const pageNumber = startPage + idx;
      if (pageNumber <= totalPages) {
        btn.textContent = pageNumber;
        btn.disabled = false;
        btn.classList.toggle("active", pageNumber === currentPage);
      } else {
        btn.textContent = "-";
        btn.disabled = true;
        btn.classList.remove("active");
      }
    });
  }

  // update last page button if exists
  if (endBtn) {
    endBtn.textContent = totalPages;
    endBtn.disabled = currentPage === totalPages;
    endBtn.classList.toggle("active", currentPage === totalPages);
  }
}
