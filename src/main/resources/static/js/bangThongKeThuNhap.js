// Common functions
function number_format(number, decimals, dec_point, thousands_sep) {
    number = (number + '').replace('.', '').replace(' ', '');
    let n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? '.' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.round(n * k) / k;
        };
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}

// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito, -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Function for updating the chart based on data
function updateChart(chart, data, labelPrefix, xLabel) {
    chart.data.labels = Object.keys(data).map(label => labelPrefix + label);
    chart.data.datasets[0].data = Object.values(data);
    chart.options.scales.xAxes[0].scaleLabel.labelString = xLabel;
    chart.update();
}

// Area Chart Example for Thang
var ctxThang = document.getElementById("bieuDoThang").getContext("2d");
var myLineChartThang = new Chart(ctxThang, {
    type: 'line',
    data: {
        labels: [],
        datasets: [{
            label: "Thu nhập",
            lineTension: 0.3,
            backgroundColor: "rgba(78, 115, 223, 0.05)",
            borderColor: "rgba(78, 115, 223, 1)",
            pointRadius: 3,
            pointBackgroundColor: "rgba(78, 115, 223, 1)",
            pointBorderColor: "rgba(78, 115, 223, 1)",
            pointHoverRadius: 3,
            pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
            pointHoverBorderColor: "rgba(78, 115, 223, 1)",
            pointHitRadius: 10,
            pointBorderWidth: 2,
            data: [],
        }],
    },
    options: {
        maintainAspectRatio: false,
        layout: {
            padding: {
                left: 10,
                right: 50,
                top: 10,
                bottom: 0
            }
        },
        scales: {
            xAxes: [{
                scaleLabel: {
                    display: false,
                    labelString: 'Tháng',
                    padding: { top: 10 }
                },
                time: {
                    unit: 'date'
                },
                gridLines: {
                    display: true,
                    drawBorder: false
                },
                ticks: {
                    maxTicksLimit: 7
                }
            }],
            yAxes: [{
                scaleLabel: {
                    display: false,
                    labelString: '₫',
                    padding: { right: 10 }
                },
                ticks: {
                    maxTicksLimit: 5,
                    padding: 10,
                    callback: function (value, index, values) {
                        if (value % 1000000 === 0) {
                            return number_format(value / 1000000) + ' triệu';
                        } else {
                            return number_format(value) + ' ₫';
                        }
                    }
                },
                gridLines: {
                    color: "rgb(234, 236, 244)",
                    zeroLineColor: "rgb(234, 236, 244)",
                    drawBorder: false,
                    borderDash: [2],
                    zeroLineBorderDash: [2]
                }
            }],
        },
        legend: {
            display: false
        },
        tooltips: {
            backgroundColor: "rgb(255,255,255)",
            bodyFontColor: "#858796",
            titleMarginBottom: 10,
            titleFontColor: '#6e707e',
            titleFontSize: 14,
            borderColor: '#dddfeb',
            borderWidth: 1,
            xPadding: 15,
            yPadding: 15,
            displayColors: false,
            intersect: false,
            mode: 'index',
            caretPadding: 10,
            callbacks: {
                label: function (tooltipItem, chart) {
                    var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                    return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + ' ₫';
                }
            }
        }
    }
});

// Area Chart Example for Ngay
var ctxNgay = document.getElementById("bieuDoNgay").getContext("2d");
var myLineChartNgay = new Chart(ctxNgay, {
    type: 'line',
    data: {
        labels: [],
        datasets: [{
            label: "Thu nhập",
            lineTension: 0.3,
            backgroundColor: "rgba(78, 115, 223, 0.05)",
            borderColor: "rgba(78, 115, 223, 1)",
            pointRadius: 3,
            pointBackgroundColor: "rgba(78, 115, 223, 1)",
            pointBorderColor: "rgba(78, 115, 223, 1)",
            pointHoverRadius: 3,
            pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
            pointHoverBorderColor: "rgba(78, 115, 223, 1)",
            pointHitRadius: 10,
            pointBorderWidth: 2,
            data: [],
        }],
    },
    options: {
        maintainAspectRatio: false,
        layout: {
            padding: {
                left: 10,
                right: 50,
                top: 10,
                bottom: 0
            }
        },
        scales: {
            xAxes: [{
                scaleLabel: {
                    display: false,
                    labelString: 'Ngày',
                    padding: { top: 10 }
                },
                time: {
                    unit: 'day'
                },
                gridLines: {
                    display: true,
                    drawBorder: false
                },
                ticks: {
                    maxTicksLimit: 7
                }
            }],
            yAxes: [{
                scaleLabel: {
                    display: false,
                    labelString: '₫',
                    padding: { right: 10 }
                },
                ticks: {
                    maxTicksLimit: 5,
                    padding: 10,
                    callback: function (value, index, values) {
                        if (value % 1000000 === 0) {
                            return number_format(value / 1000000) + ' triệu';
                        } else {
                            return number_format(value) + ' ₫';
                        }
                    }
                },
                gridLines: {
                    color: "rgb(234, 236, 244)",
                    zeroLineColor: "rgb(234, 236, 244)",
                    drawBorder: false,
                    borderDash: [2],
                    zeroLineBorderDash: [2]
                }
            }],
        },
        legend: {
            display: false
        },
        tooltips: {
            backgroundColor: "rgb(255,255,255)",
            bodyFontColor: "#858796",
            titleMarginBottom: 10,
            titleFontColor: '#6e707e',
            titleFontSize: 14,
            borderColor: '#dddfeb',
            borderWidth: 1,
            xPadding: 15,
            yPadding: 15,
            displayColors: false,
            intersect: false,
            mode: 'index',
            caretPadding: 10,
            callbacks: {
                label: function (tooltipItem, chart) {
                    var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                    return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + ' ₫';
                }
            }
        }
    }
});

// Initial data
const currentMonth = new Date().getMonth() + 1;
const currentYear = new Date().getFullYear();
document.getElementById('monthSelect').value = currentMonth;
document.getElementById('yearSelectThang').value = currentYear;
document.getElementById('yearSelectNgay').value = currentYear;

// Load dữ liệu thống kê ban đầu cho Thang và Ngay
// Load dữ liệu thống kê ban đầu
function thongKeTheoThang(year) {
    fetch('/api/thongKeTheoThang-data?year=' + year)
        .then(response => response.json())
        .then(data => {
            revenueValuesThang = data;
            updateChart(myLineChartThang, data, "Tháng ", "Tháng");
            document.getElementById('bieuDoThang').style.display = 'block';
            document.getElementById('bieuDoNgay').style.display = 'none';
            toggleHTML();
        });
}

// Load dữ liệu thống kê ban đầu
function thongKeTheoNgay(month, year) {
    fetch('/api/thongKeTheoNgay-data?month=' + month + '&year=' + year)
        .then(response => response.json())
        .then(data => {
            revenueValuesNgay = data;
            updateChart(myLineChartNgay, data, "Ngày ", "Ngày");
            document.getElementById('bieuDoThang').style.display = 'none';
            document.getElementById('bieuDoNgay').style.display = 'block';
            toggleHTML();
        });
}



window.addEventListener("load", function() {
    const currentYear = new Date().getFullYear();
    document.getElementById('yearSelectThang').value = currentYear;

    // Gọi hàm thongKeTheoThang khi toàn bộ trang HTML đã được tải
    thongKeTheoThang(currentYear);
});
// Gọi hàm để ẩn/hiện phần HTML tương ứng
function toggleHTML() {
    var bieuDoThang = document.getElementById('bieuDoThang');
    var bieuDoNgay = document.getElementById('bieuDoNgay');
    var showBtnThang = document.querySelector('.showBtnThang');
    var showBtnNgay = document.querySelector('.showBtnNgay');

    if (bieuDoThang.style.display !== 'none') {
        showBtnThang.style.display = 'block';
        showBtnNgay.style.display = 'none';
    } else if (bieuDoNgay.style.display !== 'none') {
        showBtnThang.style.display = 'none';
        showBtnNgay.style.display = 'block';
    }
}

// Gọi hàm này khi cần ẩn/hiển thị
toggleHTML();
// Thực hiện hàm AJAX để lấy dữ liệu tổng doanh thu
function updateTotalRevenue() {
    const currentYear = new Date().getFullYear();

    // Lấy dữ liệu tổng doanh thu theo tháng
    fetch('/api/thongKeTheoThang-data?year=' + currentYear)
        .then(response => response.json())
        .then(data => {
            const currentMonth = new Date().getMonth() + 1;
            const totalRevenueMonth = data[currentMonth] || 0; // Giả sử data trả về là một Map

            // Gọi hàm để cập nhật giao diện người dùng
            updateTotalRevenueUI('tongDoanhThuThang', totalRevenueMonth);
        })
        .catch(error => {
            console.error('Error fetching monthly data:', error);
        });

    fetch('/api/thongKeTheoTuan-data?year=' + currentYear)
        .then(response => response.json())
        .then(data => {
            const totalRevenueWeek = data[1] || 0; // 1 là số tuần hiện tại, có thể cần điều chỉnh
            updateTotalRevenueUI('tongDoanhThuTuan', totalRevenueWeek);
        })
        .catch(error => {
            console.error('Error fetching weekly data:', error);
        });
    // Lấy dữ liệu tổng doanh thu theo năm
    fetch('/api/thongKeTheoNam-data')
        .then(response => response.json())
        .then(data => {
            const totalRevenueNam = data || 0;

            // Gọi hàm để cập nhật giao diện người dùng
            updateTotalRevenueUI('tongDoanhThuNam', totalRevenueNam);
        })
        .catch(error => {
            console.error('Error fetching yearly data:', error);
        });
}
// Hàm định dạng số với dấu phân cách hàng nghìn và thêm đơn vị tiền tệ
function formatCurrency(value) {
    const formatter = new Intl.NumberFormat('vi', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
    });

    // Thay thế "đ" thành "₫" trong chuỗi định dạng
    return formatter.format(value).replace('₫', '₫');
}
function updateTotalRevenueUI(elementId, value) {
    document.getElementById(elementId).textContent = formatCurrency(value);
}
// Gọi hàm khi trang được load
window.addEventListener("load", function() {
    updateTotalRevenue();
});

fetch('/api/totalBills')
    .then(response => response.json())
    .then(data => {
        document.getElementById("tongDonHang").textContent = data;
    })
    .catch(error => console.error(error));


fetch('/messages/unread/total')
    .then(response => response.json())
    .then(data => {
        // Cập nhật giao diện với số lượng tin nhắn chưa đọc
        document.getElementById('countMessage').textContent = data;
    })
    .catch(error => console.error('Error fetching unread messages:', error));