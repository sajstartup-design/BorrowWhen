createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');

    // Load first page
    loadUsers(0);

    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
			createLoadingScreenBody();
            let currentPage = Number(inputPage.value);
            loadUsers(currentPage); 
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
			createLoadingScreenBody();
            let currentPage = Number(inputPage.value);
            loadUsers(currentPage - 2); 
        });
    }

    if (inputPage) {
        inputPage.addEventListener('change', () => {
			createLoadingScreenBody();
            let newPage = Number(inputPage.value);
            if (newPage < 1) newPage = 1;
            inputPage.value = newPage;
            loadUsers(newPage - 1);
        });
    }

});


async function loadUsers(page = 0) {
    try {
        const response = await fetch(`/api/users?page=${page}`);
        const data = await response.json();
		
		

        updatePagination(data.pagination);

        const tableBody = document.getElementById("table-body");
		tableBody.innerHTML = '';

        const fragment = document.createDocumentFragment();

        data.users.forEach(user => {
            const row = document.createElement("div");
            row.classList.add("table-row");
			row.setAttribute('data-id', user.encryptedId);

            row.innerHTML = `
                <div class="table-cell">${user.firstName} ${user.familyName}</div>
                <div class="table-cell">${user.userId}</div>
                <div class="table-cell">${user.phoneNumber}</div>
                <div class="table-cell">${user.emailAddress}</div>
                <div class="table-cell">${user.address}</div>
                <div class="table-cell">${user.birthDate || ""}</div>
                <div class="table-cell"><span class="role ${user.role.toLowerCase()}">${user.role}</span></div>
                <div class="table-cell">09/09/2025</div>
                <div class="table-cell">09/09/2025</div>
                <div class="table-cell">
					<button class="edit-btn" data-id="${user.encryptedId}" type="submit"><img src="/images/edit.png"></button>           
                    <button><img src="/images/delete.png"></button>
                </div>
            `;
			
			row.querySelector('.edit-btn').addEventListener('click', function(){
				const form = document.querySelector('#editForm');
				
				form.querySelector('#hiddenEncryptedId').value = this.getAttribute('data-id');
				
				form.submit();
			});
			
			row.addEventListener('click', function(e) {
			   
			    if (e.target.closest('button') || e.target.closest('a')) {
			        return; 
			    }
				
				const encryptedId = this.getAttribute('data-id');

			    window.location.href="/user/details?encryptedId=" + encryptedId;
			});


            fragment.appendChild(row);
        });

        tableBody.appendChild(fragment);

        document.querySelector(".input-page").value = data.pagination.page + 1;
		
		removeLoadingScreenBody();

    } catch (error) {
        console.error("Error fetching users:", error);
    }
}


