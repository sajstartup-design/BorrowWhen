document.addEventListener("DOMContentLoaded", () => {
	
	updateBtnsModal();
});

function updateBtnsModal(){
		const btns = document.querySelectorAll('[data-toggle="modal"]');
				
		btns.forEach(btn => {
		    btn.addEventListener("click", () => {
		        const modalTarget = btn.getAttribute("data-target");
		        const modal = document.querySelector(modalTarget);

		        if (modal) {

		            const modalBackground = document.createElement("div");
		            modalBackground.classList.add("modal-background");
					
					if(!modal.getAttribute('name')){
						if(modal.id === 'borrowModal'){
							// Fill hidden input
						 	modal.querySelector("input[name='encryptedId']").value = btn.dataset.id;

						 	modal.querySelector(".item-name").textContent = btn.dataset.name;
						  	modal.querySelector(".item-price").textContent = `₱${btn.dataset.price}`;
						  	modal.querySelector(".item-qty").textContent = btn.dataset.qty;
						}

						if(modal.id === 'approveModal' || 
							modal.id === 'rejectModal' || 
							modal.id === 'confirmModal' || 
							modal.id === 'pickUpModal' || 
							modal.id === 'issuePaymentModal' ||
							modal.id === 'cancelModal'){
								
							modal.querySelector("input[name='encryptedId']").value = btn.dataset.id;

						 	modal.querySelector(".item-name").textContent = btn.dataset.itemName;
						  	modal.querySelector(".item-price").textContent = `₱${btn.dataset.price}`;					
							modal.querySelector('.date-to-borrow').textContent = btn.dataset.dateToBorrow;
							modal.querySelector('.date-to-return').textContent = btn.dataset.dateToReturn;
							modal.querySelector('.number-to-borrow').textContent = btn.dataset.numberToBorrow;
						}

					}
					

		            document.body.appendChild(modalBackground);

		            modalBackground.appendChild(modal);

		            modalBackground.style.display = "flex";
		            modal.style.display = "flex";

		           	modalBackground.addEventListener("click", (e) => {
						if (e.target === modal || e.target.name.includes('cancel') ) {
							modal.style.display = "none";
							document.body.appendChild(modal);
		                    modalBackground.remove();
						}
		            });
		        }
		    });
		});
	}