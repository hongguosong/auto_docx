(function($) {
    'use strict';
    $(function() {
        var picFile;
        $('.file-upload-browse').on('click', function() {
            var file = $(this).parent().parent().parent().find('.file-upload-default');
            file.trigger('click');
        });
        $('.file-upload-default').on('change', function() {
            picFile = $(this)[0].files[0];    //获取需上传的文件
            $(this).parent().find('.form-control').val($(this).val().replace(/C:\\fakepath\\/i, ''));
        });

        $('#picture_upload').click(function () {
            var picName = $('#picture_name').val();
            var picDescription = $('#picture_description').val();
            var type = "picture";

            if (!picName){
                swal({
                    title: '请输入名称',
                    button: {
                        text: "OK",
                        value: true,
                        visible: true,
                        className: "btn btn-primary"
                    }
                });
                return;
            }
            if (!picDescription){
                swal({
                    title:  '请输入描述',
                    button: {
                        text: "OK",
                        value: true,
                        visible: true,
                        className: "btn btn-primary"
                    }
                });
                return;
            }
            if (!$('#picture_filename').val()){
                swal({
                    title: '请选择文件',
                    button: {
                        text: "OK",
                        value: true,
                        visible: true,
                        className: "btn btn-primary"
                    }
                });
                return;
            }

            //使用html5提供的FormData传递需上传的数据和文件
            var formData = new FormData();
            formData.append("type", type);
            formData.append("picName", picName);
            formData.append("picDescription", picDescription);
            formData.append("picFile", picFile);

            $.ajax({
                url:"http://localhost:8080/file/upload",
                type:"post",
                data:formData,
                async:false,
                processData: false,
                contentType: false,
                success:function () {
                    alert("D");
                }
            })
        })
    });


})(jQuery);
