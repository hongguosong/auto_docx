$(document).ready(function () {
    $.fn.extend({
        txtaAutoHeight: function () {
            return this.each(function () {
                var $this = $(this);
                if (!$this.attr('initAttrH')) {
                    $this.attr('initAttrH', $this.outerHeight());
                }
                setAutoHeight(this).on('input', function () {
                    setAutoHeight(this);
                });
            });
            function setAutoHeight(elem) {
                var $obj = $(elem);
                return $obj.css({ height: $obj.attr('initAttrH'), 'overflow-y': 'hidden' }).height(elem.scrollHeight);
            }
        }
    });

    var problemModal = new Vue({
        el: '#funcProblemModal',
        data: {
            edit: false,
            funcName: '',
            testCaseId: '',
            errorType:[],
            errorLevel: 0,
            environment: '',
            testCaseName: '',
            testInput: '',
            problemDesc: '',
            suggest: '',
            solution: '',
            result: '',
            testPerson: '',
            testTime: '',
            remark: '',
            other: [],
            procedureId: ''
        }
    });

    // 此处与show.bs.modal不同
    $('#funcProblemModal').on('shown.bs.modal', function (e) {
        $.each($("#funcProblemModal textarea"), function(i, n){
            $(n).css("height", n.scrollHeight + "px");
            $(n).txtaAutoHeight(); // textarea高度自适应
        })
    });

    window.problemModal = problemModal;
});