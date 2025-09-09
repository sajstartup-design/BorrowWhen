function createLoadingScreen(){
	
	const fragment = document.createDocumentFragment();
	
	const container = document.createElement('div');
	container.classList.add('loading-container');
	
	const loading = document.createElement('img');
	loading.src = '/gif/loading-gif.gif';
	
	container.append(loading);
	fragment.append(container);
	
	return fragment;
}

function createLoadingScreenBody(){
	
	if (document.querySelector('.loading-background')) return; // already exists
	
	const BODY = document.body;
	
	const fragment = document.createDocumentFragment();
	
	const background = document.createElement('div');
	background.classList.add('loading-background');
		
	const loading = document.createElement('img');
	loading.src = '/gif/loading-gif.gif';
	
	background.append(loading);
	fragment.append(background);
	
	BODY.append(fragment);
	
}	

function removeLoadingScreenBody() {
    const background = document.querySelector('.loading-background');
    if (background) {
        background.remove();
    }
}

function removeLoadingScreen(e){
	if(e){
		let loading = e.querySelector('.loading-container');
		loading.remove();
	}
}