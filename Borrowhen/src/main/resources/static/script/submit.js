document.addEventListener("DOMContentLoaded", () => {

    const updateBtn = document.querySelector('.update-btn');
    const createBtn = document.querySelector('.save-btn');
	const borrowBtn = document.querySelector('.request-btn');
	const approveBtn = document.querySelector('.approve-btn');
	const rejectBtn = document.querySelector('.reject-btn');
	const confirmBtn = document.querySelector('.confirm-btn');
	const pickUpBtn = document.querySelector('.pick-up-btn');
    const form = document.querySelector('#saveForm'); 

    if (updateBtn) {
        updateBtn.addEventListener('click', function () {
            updateBtn.textContent = 'Updating...';
            updateBtn.disabled = true;

            createLoadingScreenBody();

			const form = getForm(updateBtn);
			if (form) {
			    form.submit();
			} else {
			    console.error("❌ No form found for updateBtn");
			}
        });
    }

    if (createBtn) {
        createBtn.addEventListener('click', function () {
            createBtn.textContent = 'Saving...';
            createBtn.disabled = true;

            createLoadingScreenBody();

			const form = getForm(createBtn);
			if (form) {
			    form.submit();
			} else {
			    console.error("❌ No form found for createBtn");
			}
        });
    }
	
	if(borrowBtn){
		borrowBtn.addEventListener('click', function () {
            borrowBtn.textContent = 'Requesting...';
            borrowBtn.disabled = true;

            createLoadingScreenBody();

			const form = getForm(borrowBtn);
			if (form) {
			    form.submit();
			} else {
			    console.error("❌ No form found for borrowBtn");
			}
        });
	}
	
	if(approveBtn){
		approveBtn.addEventListener('click', function () {
            approveBtn.textContent = 'Approving...';
            approveBtn.disabled = true;

            createLoadingScreenBody();

			const form = getForm(approveBtn);
			if (form) {
			    form.submit();
			} else {
			    console.error("❌ No form found for approveBtn");
			}
        });
	}
	
	if(rejectBtn){
		rejectBtn.addEventListener('click', function () {
	        rejectBtn.textContent = 'Rejecting...';
	        rejectBtn.disabled = true;

	        createLoadingScreenBody();
			
			const form = getForm(rejectBtn);
			if (form) {
			    form.submit();
			} else {
			    console.error("❌ No form found for rejectBtn");
			}
	    });
	}
	
	if(confirmBtn){
		confirmBtn.addEventListener('click', function () {
	        confirmBtn.textContent = 'Confirming...';
	        confirmBtn.disabled = true;

	        createLoadingScreenBody();

			const form = getForm(confirmBtn);
			if (form) {
			    form.submit();
			} else {
			    console.error("❌ No form found for confirmBtn");
			}
	    });
	}
	
	if(pickUpBtn){
		pickUpBtn.addEventListener('click', function () {
	        pickUpBtn.textContent = 'Confirming...';
	        pickUpBtn.disabled = true;

	        createLoadingScreenBody();

			const form = getForm(pickUpBtn);
			if (form) {
			    form.submit();
			} else {
			    console.error("❌ No form found for pickUpBtn");
			}
	    });
	}
	
	function getForm(e) {
	    return e.form || null;
	}

});
