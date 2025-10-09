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
			  row.className = "text-gray-500 border-b border-gray-300";
			  row.setAttribute("data-id", user.encryptedId);

			  row.innerHTML = `
			    <!-- Checkbox -->
			    <td class="py-3 px-4 align-middle">
			      <input type="checkbox" class="w-5 h-5 accent-indigo-500 rounded row-select-checkbox">
			    </td>

			    <!-- Name & ID -->
			    <td class="px-4 align-middle">
			      <div class="flex items-center gap-3">
			        <div class="min-w-0">
			          <p class="text-sm font-semibold text-gray-500 truncate">${user.fullName}</p>
			          <p class="text-xs text-gray-500 truncate">${user.userId}</p>
			        </div>
			      </div>
			    </td>

			    <!-- Email -->
			    <td class="px-4 align-middle">${user.emailAddress}</td>

			    <!-- Phone -->
				<td class="px-4 align-middle">${user.phoneNumber && user.phoneNumber.trim() !== '' ? user.phoneNumber : '-'}</td>

				<!-- Birth Date -->
				<td class="px-4 align-middle">${user.birthDate && user.birthDate.trim() !== '' ? user.birthDate : '-'}</td>

			    <!-- Created -->
			    <td class="px-4 align-middle">${user.createdDate}</td>

			    <!-- Updated -->
			    <td class="px-4 align-middle">${user.updatedDate}</td>

			    <!-- Actions -->
			    <td class="px-4 align-middle">
			      <div class="flex items-center gap-3 text-gray-500">
			        <!-- View -->
			        <button class="p-1 hover:text-gray-200 view-btn" data-id="${user.encryptedId}" aria-label="View">
			          <i class="fa-regular fa-eye text-[18px]"></i>
			        </button>

			        <!-- Edit -->
			        <button class="p-1 hover:text-gray-200 edit-btn" data-id="${user.encryptedId}" aria-label="Edit">
			          <i class="fa-regular fa-pen-to-square text-[18px]"></i>
			        </button>

			        <!-- Delete -->
			        <button class="p-1 hover:text-red-400 delete-record" data-id="${user.encryptedId}" aria-label="Delete">
			          <i class="fa-regular fa-trash-can text-[18px]"></i>
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


