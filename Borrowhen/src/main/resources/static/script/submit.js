document.addEventListener("DOMContentLoaded", () => {

    const updateBtn = document.querySelector('.update-btn');
    const createBtn = document.querySelector('.save-btn');
	const borrowBtn = document.querySelector('.request-btn');
	const approveBtn = document.querySelector('.approve-btn');
    const form = document.querySelector('#saveForm'); 

    if (updateBtn) {
        updateBtn.addEventListener('click', function () {
            updateBtn.textContent = 'Updating...';
            updateBtn.disabled = true;

            createLoadingScreenBody();

            form.submit();
        });
    }

    if (createBtn) {
        createBtn.addEventListener('click', function () {
            createBtn.textContent = 'Saving...';
            createBtn.disabled = true;

            createLoadingScreenBody();

            form.submit();
        });
    }
	
	if(borrowBtn){
		borrowBtn.addEventListener('click', function () {
            borrowBtn.textContent = 'Requesting...';
            borrowBtn.disabled = true;

            createLoadingScreenBody();

            form.submit();
        });
	}
	
	if(approveBtn){
		approveBtn.addEventListener('click', function () {
            approveBtn.textContent = 'Approving...';
            approveBtn.disabled = true;

            createLoadingScreenBody();

            form.submit();
        });
	}

});
