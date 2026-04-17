const mobileBtn = document.getElementById("mobile-button");
const mobileNavList = document.querySelector(".header__inner-list-mobile");

mobileBtn.addEventListener("click", (e) => {
	mobileNavList.classList.toggle("show-inner-list-mobile");
});
