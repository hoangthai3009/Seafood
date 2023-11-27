$(document).ready(function (){
    $('#mainImage').change(function (){
        showImageThumbnail(this);
    });
    $("input[name='extraImage']").each(function(index){
        $(this).change(function (){
            showExtraImageThumbnail(this,index);
        });
    });
});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    var thumbnail = $('#thumbnail');
    var fileNamePlaceholder = $('#fileNamePlaceholder');

    reader.onload = function (e) {
        thumbnail.attr('src', e.target.result);
    };

    if (file) {
        reader.readAsDataURL(file);
        // Cập nhật tên file đã chọn
        fileNamePlaceholder.text(file.name);
    } else {
        // Nếu không có file nào được chọn
        fileNamePlaceholder.text('Chưa có file nào được chọn');
    }
}

function showExtraImageThumbnail(fileInput, index){
    file = fileInput.files[0];
    reader = new FileReader();
    reader.onload = function(e){
        $('#thumbnail' + index).attr('src', e.target.result);
    };
    reader.readAsDataURL(file);
}

function openTab(tabName) {
    var i, tabcontent;
    tabcontent = document.getElementsByClassName("tab");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].classList.remove("active-tab");
    }

    document.getElementById(tabName).classList.add("active-tab");
}

document.addEventListener("DOMContentLoaded", function (event) {
    document.querySelectorAll('img').forEach(function (img) {
        img.onerror = function () {
            this.style.display = 'none';
        };
    })
});