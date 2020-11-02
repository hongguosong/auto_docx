(function($) {
    'use strict';
    var form = $("#example-form");
    form.children("div").steps({
        headerTag: "h3",
        bodyTag: "section",
        transitionEffect: "slideLeft",
        onFinished: function(event, currentIndex) {
            alert("提交中!");
        },
        labels: {
            finish: "完成",
            next: "下一步",
            previous: "上一步"
        }
    });
    var validationForm = $("#example-validation-form");
    validationForm.val({
        errorPlacement: function errorPlacement(error, element) {
            element.before(error);
        },
        rules: {
            confirm: {
                equalTo: "#password"
            }
        }
    });
    validationForm.children("div").steps({
        headerTag: "h3",
        bodyTag: "section",
        transitionEffect: "slideLeft",
        onStepChanging: function(event, currentIndex, newIndex) {
            validationForm.val({
                ignore: [":disabled", ":hidden"]
            })
            return validationForm.val();
        },
        onFinishing: function(event, currentIndex) {
            validationForm.val({
                ignore: [':disabled']
            })
            return validationForm.val();
        },
        onFinished: function(event, currentIndex) {
            alert("提交中!");
        }
    });
    var verticalForm = $("#example-vertical-wizard");
    verticalForm.children("div").steps({
        headerTag: "h3",
        bodyTag: "section",
        transitionEffect: "slideLeft",
        stepsOrientation: "vertical",
        onFinished: function(event, currentIndex) {
            alert("提交中!");
        }
    });
})(jQuery);