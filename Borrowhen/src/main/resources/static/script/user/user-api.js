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
      const params = new URLSearchParams({
         page,
         search
      });

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
			  <td class="py-4 px-2 text-xs align-middle">
			    <div class="flex items-center justify-center">
			      <input type="checkbox" class="w-3 h-3 accent-indigo-500 rounded row-select-checkbox">
			    </div>
			  </td>
			  <td class="px-4 align-middle">
			  <div class="flex items-center gap-3">
			    <div class="min-w-0">
			      <p class="text-sm font-semibold text-gray-500 truncate">${user.fullName}</p>
			      <p class="text-xs text-gray-500 truncate">@${user.userId}</p>
			    </div>
			  </div>
			</td>
			  <td class="px-2 text-xs align-middle">${user.emailAddress}</td>
			  <td class="px-2 text-xs align-middle">${user.phoneNumber && user.phoneNumber.trim() !== '' ? user.phoneNumber : '-'}</td>
			  <td class="px-2 text-xs align-middle">${user.birthDate && user.birthDate.trim() !== '' ? user.birthDate : '-'}</td>
			  <td class="px-2 text-xs align-middle">${user.createdDate}</td>
			  <td class="px-2 text-xs align-middle">${user.updatedDate}</td>
			  <td class="px-2 text-sm align-middle">
			    <div class="flex items-center gap-2 text-gray-500">

			      <!-- View Button -->
			      <div class="tooltip-wrapper">
			        <a
			          href="#"
			          class="view-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-blue-100 hover:bg-blue-200 transition shadow-sm"
			          data-id="${user.encryptedId}"
			          aria-label="View"
			        >
			          <img src="/images/view.png" alt="View" class="h-3 w-3" />
			        </a>
			        <span class="tooltip-text">View User</span>
			      </div>

			      <!-- Edit Button -->
			      <div class="tooltip-wrapper">
			        <a
			          href="#"
			          class="edit-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-yellow-100 hover:bg-yellow-200 transition shadow-sm"
			          data-id="${user.encryptedId}"
			          aria-label="Edit"
			        >
			          <img src="/images/edit.png" alt="Edit" class="h-3 w-3" />
			        </a>
			        <span class="tooltip-text">Edit User</span>
			      </div>

			      <!-- Delete Button -->
			      <div class="tooltip-wrapper">
			        <a
			          href="#"
			          class="delete-record border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-red-100 hover:bg-red-200 transition shadow-sm"
			          data-id="${user.encryptedId}"
			          aria-label="Delete"
			        >
			          <img src="/images/delete.png" alt="Delete" class="h-3 w-3" />
			        </a>
			        <span class="tooltip-text">Delete User</span>
			      </div>

			    </div>
			  </td>

			`;

         row.querySelector('.edit-btn').addEventListener('click', function () {
            const form = document.querySelector('#editForm');

            form.querySelector('#hiddenEncryptedId').value = this.getAttribute('data-id');

            form.submit();
         });

         row.querySelector('.view-btn').addEventListener('click', function () {

            const encryptedId = this.getAttribute('data-id');

            window.location.href = "/admin/user/details?encryptedId=" + encryptedId;
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