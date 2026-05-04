document.addEventListener('DOMContentLoaded', function() {
    
    const allSearchBtns = document.querySelectorAll('.searchBtn');
    const searchBar = document.querySelector('.searchBar');
    const searchInput = document.getElementById('searchInput');
    const searchClose = document.getElementById('searchClose');

    const openSearch = () => {
        searchBar.classList.add('open');
        allSearchBtns.forEach(btn => btn.setAttribute('aria-expanded', 'true'));
        setTimeout(() => searchInput.focus(), 100);
    };

    const closeSearch = () => {
        searchBar.classList.remove('open');
        allSearchBtns.forEach(btn => btn.setAttribute('aria-expanded', 'false'));
    };

    allSearchBtns.forEach(btn => {
        btn.addEventListener('click', openSearch);
    });

    searchClose.addEventListener('click', closeSearch);

    // Close on ESC key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && searchBar.classList.contains('open')) {
            closeSearch();
        }
    });

});
