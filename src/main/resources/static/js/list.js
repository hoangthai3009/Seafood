/*$(document).ready(function () {
    $.ajax( {
        url: 'http://localhost:8080/api/v1/books',
        type: 'GET',
        dataType: 'json',
            success: function (data) {
            let trHTML = '';
            $.each(data, function (i, item) {
                trHTML = trHTML + '<tr id="book-' + item.id + '">' +
                        '<td>' + item.id + '</td>' +
                        '<td>' + item.title + '</td>' +
                        '<td>' + item.author + '</td>' +
                        '<td>' + item.price + '</td>' +
                        '<td>' + item.categoryName + '</td>' +
                        '<td sec:authorize="hasAnyAuthority(\'ADMIN\')">' +
                            '<a href="/books/edit/' + item.id + '" class="text-primary">Edit</a> | '+
                            '<a href="/books"'+ item.id + ' class="text-danger" onclick="apiDeleteBook(' + item.id + ',this); return false;">Delete</a> | ' +
                            '<form action="/books/add-to-cart" method="post" class="d-inline">' +
                                '<input type="hidden" name="id" value="' + item.id + '">' +
                                '<input type="hidden" name="name" value="' + item.title + '">' +
                                '<input type="hidden" name="price" value="' + item.price + '">' +
                                '<button type="submit" class="btn btn-success">Add to cart</button>' +
                            '</form>' +
                        '</td>' +
                    '</tr>';
            });
            $('#book-table-body').append(trHTML);
        }
    });
});*/

function apiDeleteBook (id) {
    if (confirm('Are you sure you want to delete this book?')) {
        $.ajax( {
            url: 'http://localhost:8080/api/v1/books/' + id,
            type: 'DELETE',
            success: function () {
                alert('Book deleted successfully!');
                $('#book-' + id).remove();
            }
        });
    }
}