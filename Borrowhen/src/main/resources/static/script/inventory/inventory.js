async function loadRequests(page = 0, search = "") {
   try {
      const params = new URLSearchParams({ page, search });
      const url = `/api/request?${params.toString()}`;
      const response = await fetch(url);
      const data = await response.json();

      const tableBody = document.getElementById("table-body");
      tableBody.innerHTML = "";

      const fragment = document.createDocumentFragment();

      if (data.requests && data.requests.length > 0) {
         data.requests.forEach((request) => {
            const status = request.status?.toLowerCase().replace(" ", "").trim();

            // Status style
            let statusColor = "";
            let statusTextColor = "";
            if (status === "pending") {
               statusColor = "bg-yellow-100";
               statusTextColor = "text-yellow-700";
            } else if (status === "approved" || status === "pick-upready" || status === "completed") {
               statusColor = "bg-green-100";
               statusTextColor = "text-green-700";
            } else if (status === "rejected") {
               statusColor = "bg-red-100";
               statusTextColor = "text-red-700";
            } else {
               statusColor = "bg-gray-100";
               statusTextColor = "text-gray-700";
            }

            // Action Buttons
            let actionButtons = "";
            if (status === "pending") {
               actionButtons = buttons.cancel(request) + buttons.fake("received-icon");
            } else if (status === "pick-upready") {
               actionButtons = buttons.fake("cancelled") + buttons.received(request);
            } else {
               actionButtons = buttons.fake("cancelled") + buttons.fake("received-icon");
            }

            // Row
            const row = document.createElement("tr");
            row.className = "hover:bg-gray-50 transition";
            row.setAttribute("data-id", request.encryptedId);

            row.innerHTML = `
               <td class="px-4 py-3 text-gray-800 text-sm font-medium">${request.itemName}</td>
               <td class="px-4 py-3 text-gray-700 text-sm">₱${request.price}</td>
               <td class="px-4 py-3 text-gray-700 text-sm">${request.qty}</td>
               <td class="px-4 py-3 text-gray-700 text-sm">${request.dateToBorrow}</td>
               <td class="px-4 py-3 text-gray-700 text-sm">${request.dateToReturn}</td>
               <td class="px-4 py-3">
                  <span class="px-3 py-1 text-xs font-medium rounded-full ${statusColor} ${statusTextColor}">
                     ${request.status}
                  </span>
               </td>
               <td class="px-4 py-3 text-sm text-gray-700 flex items-center gap-2">
                  ${actionButtons}
               </td>
            `;

            // Click listener
            row.addEventListener("click", function (e) {
               if (e.target.closest("button") || e.target.closest("a")) return;
               const encryptedId = this.getAttribute("data-id");
               window.location.href = "/admin/inventory/details?encryptedId=" + encryptedId;
            });

            fragment.appendChild(row);
         });

         tableBody.appendChild(fragment);
         updateBtnsModal();
      } else {
         tableBody.innerHTML = `
            <tr>
               <td colspan="7" class="text-center py-6 text-gray-500 text-sm">No requests found.</td>
            </tr>
         `;
      }

      removeLoadingScreenBody();
   } catch (error) {
      console.error("Error fetching requests:", error);
   }
}
