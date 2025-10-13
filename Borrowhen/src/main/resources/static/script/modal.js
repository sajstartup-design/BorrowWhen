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

			if (modal) {
				const modalBackground = document.createElement("div");
				
				document.body.appendChild(modalBackground);

				if (!modal.getAttribute("name")) {
					if (modal.id === "borrowModal") {
						modal.querySelector("#encryptedId").value = btn.dataset.id;
						modal.querySelector(".item-name").textContent = btn.dataset.name;
						modal.querySelector(".item-price").textContent = `₱${btn.dataset.price}`;
						modal.querySelector(".item-qty").textContent = btn.dataset.qty;
					}

					if (
						["approveModal", "rejectModal", "confirmModal", "pickUpModal", "issuePaymentModal", "cancelModal"].includes(modal.id)
					) {
						modal.querySelector("input[name='encryptedId']").value = btn.dataset.id;
						modal.querySelector(".item-name").textContent = btn.dataset.itemName;
						modal.querySelector(".item-price").textContent = `₱${btn.dataset.price}`;
						modal.querySelector(".date-to-borrow").textContent = btn.dataset.dateToBorrow;
						modal.querySelector(".date-to-return").textContent = btn.dataset.dateToReturn;
						modal.querySelector(".number-to-borrow").textContent = btn.dataset.numberToBorrow;
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
