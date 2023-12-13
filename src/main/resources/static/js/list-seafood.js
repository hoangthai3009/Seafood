document.addEventListener('DOMContentLoaded', function () {
    const sortButtons = document.getElementById('sortButtons').querySelectorAll('button');
    const seafoodList = document.getElementById('listSeafoods');
    const paginationContainer = document.querySelector('.product__pagination');
    const urlParams = new URLSearchParams(window.location.search);
    const categoryId = urlParams.get('categoryId');
    const keyword = urlParams.get('keyword');

    // Hàm để hiển thị sản phẩm
    function displaySeafood(seafood) {
        const column = document.createElement("div");
        column.className = "col-lg-3 col-md-4 col-sm-6 mix oranges fresh-meat";
        column.innerHTML = `
            <div class="featured__item" data-filter="${seafood.category.id}">
                <div class="featured__item__pic" href="${seafood.id}">
                    <a href="/seafood/${seafood.id}">
                        <img src="${seafood.mainImage}" alt="Hình ảnh sản phẩm">
                    </a>
                    <ul class="featured__item__pic__hover">
                        <li><button><i class="fas fa-heart"></i></button></li>
                        <li>
                            <button class="addToCartButton" onclick="updateCartItemCount()">
                                <i class="fas fa-shopping-cart"></i>
                            </button>
                        </li>
                    </ul>
                </div>
                <div class="featured__item__text">
                    <h6><a href="${seafood.id}" title="${seafood.name}">${seafood.name}</a></h6>
                    <h5>${seafood.price.toLocaleString('vi-VN')} ₫</h5>
                </div>
            </div>`;

        seafoodList.appendChild(column);

        // Gán sự kiện click cho nút thêm vào giỏ hàng sau khi thêm vào DOM
        const addToCartButton = column.querySelector(".addToCartButton");
        addToCartButton.addEventListener("click", function() {
            const confirmation = confirm('Bạn có muốn thêm sản phẩm này vào giỏ hàng?');
            if (confirmation) {
                addToCart(seafood.id, seafood.name, seafood.price, seafood.mainImage, 1, seafood.category.name);
            }
        });
    }

    // Gọi API và cập nhật UI
    function fetchData(pageNumber = 1, sortValue = null, keyword, categoryId) {
        let apiUrl = `/api/seafoods?page=${pageNumber - 1}&size=8`;

        // Thêm tham số sort nếu sortValue khác null
        if (sortValue) {
            apiUrl += `&sort=${sortValue}`;
        }

        // Thêm tham số keyword nếu keyword khác null
        if (keyword) {
            apiUrl += `&keyword=${encodeURIComponent(keyword)}`;
        }

        // Thêm tham số categoryId nếu có
        if (categoryId) {
            apiUrl = `/api/seafoods/category?categoryId=${categoryId}&page=${pageNumber - 1}&size=8&keyword=`;
        }

        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                seafoodList.innerHTML = ''; // Xóa danh sách hiện tại
                if (data.content && Array.isArray(data.content)) {
                    data.content.forEach(seafood => {
                        displaySeafood(seafood);
                    });
                    updatePagination(data.totalPages, pageNumber);
                } else {
                    // Xử lý trường hợp không có dữ liệu hoặc dữ liệu không hợp lệ
                    console.log("Không có dữ liệu hoặc dữ liệu không hợp lệ");
                }
            })
            .catch(error => console.error('Error fetching data:', error));
    }

    // Sự kiện khi click vào nút tìm kiếm
    document.getElementById("searchButton").addEventListener("click", function (event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định của nút button

        const keyword = document.getElementById("searchInput").value;
        fetchData(1, getCurrentSortValue(), keyword,categoryId);
    });

    // Gọi fetchData với categoryId lấy từ tham số URL (nếu có)
    fetchData(1, getCurrentSortValue(), keyword, categoryId);

    // Các hàm và sự kiện khác
    // ...

    // Cập nhật nút phân trang
    function updatePagination(totalPages, currentPage) {
        paginationContainer.innerHTML = ''; // Xóa nút phân trang cũ

        for (let i = 1; i <= totalPages; i++) {
            const pageLink = document.createElement('a');
            pageLink.href = '#';
            pageLink.textContent = i;
            if (i === currentPage) {
                pageLink.classList.add('active');
            }
            pageLink.addEventListener('click', function(e) {
                e.preventDefault();
                fetchData(i, getCurrentSortValue()); // Trang số i và giá trị sắp xếp hiện tại
            });
            paginationContainer.appendChild(pageLink);
        }

        // Thêm nút điều hướng "Tiếp theo"
        if (currentPage < totalPages) {
            const nextLink = document.createElement('a');
            nextLink.href = '#';
            nextLink.innerHTML = '<i class="fa fa-long-arrow-right"></i>';
            nextLink.addEventListener('click', function(e) {
                e.preventDefault();
                fetchData(currentPage + 1, getCurrentSortValue()); // Trang tiếp theo và giá trị sắp xếp hiện tại
            });
            paginationContainer.appendChild(nextLink);
        }
    }

    // Lấy giá trị sắp xếp hiện tại
    function getCurrentSortValue() {
        for (const button of sortButtons) {
            if (button.classList.contains('active')) {
                return button.getAttribute('data-value');
            }
        }
        // Mặc định là 'null' nếu không tìm thấy giá trị sắp xếp hiện tại
        return '';
    }

    // Xử lý sự kiện khi click nút sắp xếp
    sortButtons.forEach(button => {
        button.addEventListener('click', function () {
            sortButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            fetchData(1, button.getAttribute('data-value'), null);
        });
    });

});
