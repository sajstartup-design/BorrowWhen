function createLoadingScreen() {
  const fragment = document.createDocumentFragment();

  const container = document.createElement('div');
  container.className = `
    fixed inset-0 flex flex-col items-center justify-center 
    bg-white/80 backdrop-blur-sm z-50
  `;

  const loaderWrapper = document.createElement('div');
  loaderWrapper.className = "flex flex-col items-center space-y-4";

  const loadingImg = document.createElement('img');
  loadingImg.src = '/gif/loading-gif.gif';
  loadingImg.alt = 'Loading...';
  loadingImg.className = "w-20 h-20 animate-pulse";

  const quote = document.createElement('p');
  quote.textContent = "“Patience is not the ability to wait, but how you act while waiting.”";
  quote.className = "text-gray-600 text-sm italic text-center mt-2";

  loaderWrapper.append(loadingImg, quote);
  container.append(loaderWrapper);
  fragment.append(container);

  return fragment;
}


function createLoadingScreenBody() {
  if (document.querySelector('.loading-background')) return; // prevent duplicates

  const BODY = document.body;
  const fragment = document.createDocumentFragment();

  const background = document.createElement('div');
  background.className = `
    loading-background fixed inset-0 flex flex-col items-center justify-center 
    bg-gray-100/90 backdrop-blur-sm z-50
  `;

  const loadingImg = document.createElement('img');
  loadingImg.src = '/gif/loading-gif.gif';
  loadingImg.alt = 'Loading...';
  loadingImg.className = "w-24 h-24 animate-pulse mb-3";

  const quote = document.createElement('p');
  quote.textContent = "“Good things take time. Hang tight...”";
  quote.className = "text-gray-600 text-sm italic text-center";

  background.append(loadingImg, quote);
  fragment.append(background);

  BODY.append(fragment);
}


function removeLoadingScreenBody() {
  const background = document.querySelector('.loading-background');
  if (background) {
    background.classList.add('opacity-0', 'transition-opacity', 'duration-300');
    setTimeout(() => background.remove(), 300);
  }
}


function removeLoadingScreen(e) {
  if (e) {
    const loading = e.querySelector('.loading-container');
    if (loading) {
      loading.classList.add('opacity-0', 'transition-opacity', 'duration-300');
      setTimeout(() => loading.remove(), 300);
    }
  }
}

document.addEventListener("DOMContentLoaded", () => {
	
	const buttons = document.querySelectorAll('.transitioning')
	
	buttons.forEach(btn => btn.addEventListener('click', function(){
		createLoadingScreenBody();
	}));
});
