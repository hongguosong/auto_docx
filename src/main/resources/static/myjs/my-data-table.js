(function($) {
    'use strict';
    $(function() {

        //初始化dataTable
       var table = $('#my_data_table').DataTable({
            "data":[
                //使用对象数组添加数据时需要用到  "columns":[]
                // {
                //     "picture_id": 1,
                //     "picture_name": 2,
                //     "picture_description": 3,
                //     "picture_path":4,
                //     "picture_operation": null
                // }

                //使用二维数组添加数据时不需要用到 "columns":[]
                ["1", "a", "a", "a", null],
                ["2", "b", "b", "b", null],
                ["3", "c", "c", "c", null],
                ["4", "d", "d", "d", null],
                ["5", "e", "e", "e", null],
                ["6", "f", "f", "f", null],
                ["7", "g", "g", "g", null],
                ["8", "h", "h", "h", null],
                ["9", "i", "i", "i", null],
                ["10", "j", "j", "j", null],
                ["11", "k", "k", "k", null]
            ],

            // "columns":[
            //     {"data": "picture_id"},
            //     {"data": "picture_name"},
            //     {"data": "picture_description"},
            //     {"data": "picture_path"},
            //     {"data": "picture_operation"}
            // ],

            "columnDefs":[
                {
                    "targets": -1,    //"targets"取值-1表示操作最后一列数据。操作其他列数据时，第一列下标为0
                    "data": null,
                    "render": function (data, type, full, meta) {
                        // return "<button id='update' type='button' class='btn btn-outline-success'>编辑</button><button id='delete' type='button' class='btn btn-outline-danger'>删除</button>"

                        //data-toggle指以什么事件触发，常用的如modal,popover,tooltips等，data-target指事件的目标，
                        // 一起使用就是代表data-target所指的元素以data-toggle指定的形式显示
                        return "<button id='update' type='button' data-toggle='modal' data-target='#my_modal' class='btn btn-outline-success'>编辑</button>" +
                            "<button id='delete' type='button' class='btn btn-outline-danger'>删除</button>"

                    }
                }
            ],

            //将语言设置成中文
            "language": {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项数据",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项数据，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项数据，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项数据过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上一页",
                    "sNext": "下一页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": "以升序排列此列",
                    "sSortDescending": "以降序排列此列"
                }
            }
        });

        //直接使用$("#delete").click()方法只能获取第一个delete按钮的点击事件
        // $("#delete").click(function () {
        //     alert("delete");
        // })

        //使用 $("#my_data_table").on()方法可以获取到所有delete按钮的点击事件
        table.on('click', 'button#delete', function (e) {

            var row = table.row($(this).parents("tr"));  //获取当前行对象

            //弹出确定删除对话框
            swal({
                title: '确定删除？',
                text: null,
                icon: 'warning',
                // showCancelButton: true,
                // confirmButtonColor: '#3f51b5',
                cancelButtonColor: '#ff4081',
                // confirmButtonText: 'Great ',
                buttons: {
                    cancel: {
                        text: "取消",
                        value: null,
                        visible: true,
                        className: "btn btn-danger",
                        closeModal: true
                    },
                    confirm: {
                        text: "确定",
                        value: true,
                        visible: true,
                        className: "btn btn-primary",
                        closeModal: true
                    }
                }
            }).then(function (isConfirm) {
                if (isConfirm){
                    row.remove().draw(false);     //执行删除操作
                }
            });

        });

        //获取所有update按钮的点击事件
        table.on('click', 'button#update', function () {
            var row = table.row($(this).parents("tr"))[0];  //获取当前行对象
            var data = table.row(row).data();   //获取当前行数据的数组[]
            // console.log(data);

            // console.log($(this).parents('tr').children('td').eq(0).text());   //获取当前行第一个td内容

            //获取当前行的所有td标签
            var $td = $(this).parents('tr').children('td');

            //设置modal编辑框中的初始值
            $('#name').val(data[1]);
            $('#description').val(data[2]);

            $('#yes').click(function () {
                // var currentData = [$('#id').val(), $('#name').val(), $('#description').val(), $('#path').val(), null]
                // console.log(currentData);

                //执行修改操作
                // $td.eq(0).html($('#id').val());
                // $td.eq(1).html($('#name').val());
                // $td.eq(2).html($('#description').val());
                // $td.eq(3).html($('#path').val());

                swal({
                    title: '确定修改？',
                    text: null,
                    icon: 'warning',
                    // showCancelButton: true,
                    // confirmButtonColor: '#3f51b5',
                    cancelButtonColor: '#ff4081',
                    // confirmButtonText: 'Great ',
                    buttons: {
                        cancel: {
                            text: "取消",
                            value: null,
                            visible: true,
                            className: "btn btn-danger",
                            closeModal: true
                        },
                        confirm: {
                            text: "确定",
                            value: true,
                            visible: true,
                            className: "btn btn-primary",
                            closeModal: true
                        }
                    }
                }).then(function (isConfirm) {
                    if (isConfirm){

                        $td.eq(1).html($('#name').val());
                        $td.eq(2).html($('#description').val());

                        //关闭modal
                        $('#my_modal').modal('hide');
                    }
                });
            });
        });
    });
})(jQuery);
