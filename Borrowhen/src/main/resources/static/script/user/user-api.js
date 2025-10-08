createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');
	const search = document.querySelector('.search');


    // Load first page
    loadUsers(0);

    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
			createLoadingScreenBody();
			const searchValue = search.value;
            let currentPage = Number(inputPage.value);
            loadUsers(currentPage, searchValue); 
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
			createLoadingScreenBody();
			const searchValue = search.value;
            let currentPage = Number(inputPage.value);
            loadUsers(currentPage - 2, searchValue); 
        });
    }

    if (inputPage) {
        inputPage.addEventListener('change', () => {
			createLoadingScreenBody();
			const searchValue = search.value;
            let newPage = Number(inputPage.value);
            if (newPage < 1) newPage = 1;
            inputPage.value = newPage;
            loadUsers(newPage - 1, searchValue);
        });
    }
	
	if (search) {
	    let typingTimer; 
	    const delay = 500; 

	    search.addEventListener('input', function () {
	        clearTimeout(typingTimer); 

	        const currentPage = 0;
	        const searchValue = this.value;

	        typingTimer = setTimeout(() => {
				createLoadingScreenBody();
	            loadUsers(currentPage, searchValue);
	        }, delay);
	    });
	}

});


async function loadUsers(page = 0,
	search = ""
) {
    try {
		const params = new URLSearchParams({ page, search });
				
		const url = `/api/admin/users?${params.toString()}`;
				
        const response = await fetch(url);
        const data = await response.json();
		
        updatePagination(data.pagination);

        const tableBody = document.getElementById("table-body");
		tableBody.innerHTML = '';

        const fragment = document.createDocumentFragment();

        data.users.forEach(user => {
			const row = document.createElement("tr");
			row.classList.add("border-gray-200")
            row.classList.add("border-b");
			row.setAttribute('data-id', user.encryptedId);
			
			row.innerHTML = `
				<td><input type="checkbox" class="checkbox checkbox-sm checkbox-primary row-select-checkbox" ></td>
				<td>
				  <div class="flex flex-col">
				    <span class="font-medium">${user.fullName}</span>
				    <span class="text-sm text-gray-500">${user.userId}</span>
				  </div>
				</td>	              
				<td>${user.emailAddress}</td>
				<td>${user.phoneNumber}</td>
              	<td>${user.birthDate}</td>
              	<td>${user.createdDate}</td>
              	<td>${user.updatedDate}</td>
              	<td>
					<div class="flex items-center gap-2">
					  <button type="button" class="btn btn-circle btn-text btn-sm view-btn" data-id="${user.encryptedId}" title="View Details">
					    <span class="icon-[tabler--eye] size-5 text-base-content"></span>
					  </button>
	
					  <button type="button" class="btn btn-circle btn-text btn-sm edit-btn" data-id="${user.encryptedId}" title="Edit">
					    <span class="icon-[tabler--edit] size-5 text-base-content"></span>
					  </button>
	
					  <button type="button" class="btn btn-circle btn-text btn-sm delete-record" data-id="${user.encryptedId}" title="Delete">
					    <span class="icon-[tabler--trash] size-5 text-base-content"></span>
					  </button>
					</div>
              	</td>
            `;

/*            row.innerHTML = `
                <div class="table-cell">${user.firstName} ${user.familyName}</div>
                <div class="table-cell">${user.userId}</div>
                <div class="table-cell">${user.phoneNumber}</div>
                <div class="table-cell">${user.emailAddress}</div>
                <div class="table-cell">${user.address}</div>
                <div class="table-cell">${user.birthDate || ""}</div>
                <div class="table-cell"><span class="role ${user.role.toLowerCase()}">${user.role}</span></div>
                <div class="table-cell">${user.createdDate}</div>
                <div class="table-cell">${user.updatedDate}</div>
                <div class="table-cell">
					<button class="edit-btn darker" data-id="${user.encryptedId}" type="submit"><img src="/images/edit.png"></button>           
					${
		                user.isDeletable
		                    ? `<button class="delete-btn darker" data-id="${user.encryptedId}" type="submit"><img src="/images/delete.png"></button>`
		                    : `<button class="delete-btn darker disabled" disabled title="User has pending requests"><img src="/images/delete.png"></button>`
		            }
                </div>
            `;*/
			
			row.querySelector('.edit-btn').addEventListener('click', function(){
				const form = document.querySelector('#editForm');
				
				form.querySelector('#hiddenEncryptedId').value = this.getAttribute('data-id');
				
				form.submit();
			});
			
			row.querySelector('.view-btn').addEventListener('click', function() {
			   				
				const encryptedId = this.getAttribute('data-id');

			    window.location.href="/admin/user/details?encryptedId=" + encryptedId;
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


