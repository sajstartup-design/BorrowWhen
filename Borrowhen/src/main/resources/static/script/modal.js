document.addEventListener("DOMContentLoaded", () => {
	updateBtnsModal();

	const modals = document.querySelectorAll(".pop-up-modal");

	if (modals.length > 0) {
		modals.forEach(modal => {
			
			modal.addEventListener('click', function(e){
				if (e.target === modal || e.target.name?.includes("cancel")) {
					// Start pop-out animation
					modal.classList.remove("animate-pop-in");
					modal.classList.add("animate-pop-out");

					// Wait for animation to finish before hiding
					modal.addEventListener(
						"animationend",
						() => {
							modal.style.display = "none";
							modal.classList.remove("animate-pop-out");
							document.body.appendChild(modal);
							modalBackground.remove();
						},
						{ once: true }
					);	
				}
			});
		});
	}
});


function updateBtnsModal() {
	const btns = document.querySelectorAll('[data-toggle="modal"]');
	
	console.log(btns);

	btns.forEach(btn => {
		btn.addEventListener("click", () => {
			const modalTarget = btn.getAttribute("data-target");
			const modal = document.querySelector(modalTarget);
			
			console.log(modal.id);

			if (modal) {
				
				const form = modal.querySelector("form");
			    const submitBtn = modal.querySelector("button[type='submit']");
			
			    if (form && submitBtn) {
			        form.addEventListener("submit", () => {
			            submitBtn.disabled = true;
			            submitBtn.textContent = "PROCESSING...";
			        });
			    }
				
				const modalBackground = document.createElement("div");
				
				document.body.appendChild(modalBackground);

				if (!modal.getAttribute("name")) {
					if (modal.id === "borrowModal") {
						modal.querySelector("#encryptedId").value = btn.dataset.id;
						modal.querySelector(".item-name").textContent = btn.dataset.name;
						modal.querySelector(".item-price").textContent = `₱${btn.dataset.price}`;
						modal.querySelector(".item-qty").textContent = btn.dataset.qty;
						modal.querySelector(".image").src = `/view/image/${btn.dataset.image}`;
					}

					if (
						["approveModal", "rejectModal", "confirmModal", "receiveModal", "returnModal", "pickUpModal", "issuePaymentModal", "cancelModal"].includes(modal.id)
					) {
						modal.querySelector("input[name='encryptedId']").value = btn.dataset.id;
						modal.querySelector(".item-name").textContent = btn.dataset.itemName;
						modal.querySelector(".item-price").textContent = `₱${btn.dataset.price}`;
						modal.querySelector(".date-to-borrow").textContent = btn.dataset.dateToBorrow;
						modal.querySelector(".date-to-return").textContent = btn.dataset.dateToReturn;
						modal.querySelector(".number-to-borrow").textContent = btn.dataset.numberToBorrow;
						modal.querySelector(".purpose").textContent = btn.dataset.purpose;
						
						const imgEl = modal.querySelector(".image");
					    const imgPath = `/view/image/${btn.dataset.image}`;
					    
					    const tempImg = new Image();
					    tempImg.onload = function() {
					        imgEl.src = imgPath; // Image exists, set the src
					    };
					    tempImg.onerror = function() {
					        imgEl.src = `/view/image/no_image.jpg`; // Image doesn't exist, set fallback
					    };
					    tempImg.src = imgPath;
					}
					
					if(
						
						["deleteModal", "feedbackModal"].includes(modal.id)
					) {

					    modal.querySelector("input[name='encryptedId']").value = btn.dataset.id;
						
						if(modal.id === "deleteModal"){
							modal.querySelector("#deleteName").textContent = btn.dataset.name;
						}
						
					}
				}

				// 🟦 Show Modal with Pop-in Animation
				modalBackground.appendChild(modal);
				modal.style.display = "flex";
				modal.classList.add("animate-pop-in");

				// 🟥 Close modal logic (click background or cancel)
				modal.addEventListener("click", e => {
					if (e.target === modal || e.target.name?.includes("cancel")) {
						// Start pop-out animation
						modal.classList.remove("animate-pop-in");
						modal.classList.add("animate-pop-out");

						// Wait for animation to finish before hiding
						modal.addEventListener(
							"animationend",
							() => {
								modal.style.display = "none";
								modal.classList.remove("animate-pop-out");
								document.body.appendChild(modal);
								modalBackground.remove();
							},
							{ once: true }
						);
					}
				});
			}
		});
	});
}
