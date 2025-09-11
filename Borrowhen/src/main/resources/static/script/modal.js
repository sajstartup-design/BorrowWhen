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