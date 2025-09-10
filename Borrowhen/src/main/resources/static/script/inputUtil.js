document.querySelector(".price-input").addEventListener("input", function () {
    let cleaned = this.value
        .replace(/[^0-9.]/g, "")     
        .replace(/(\..*?)\..*/g, "$1"); 

    if (cleaned) {
        this.value = "₱" + cleaned;
    } else {
        this.value = ""; 
    }
});