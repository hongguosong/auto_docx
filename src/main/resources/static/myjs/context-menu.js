(function($) {
    'use strict';
    $.contextMenu({
        selector: '#analyze-info',
        callback: function(key, options) {

        },
        items: {
            "edit": {
                name: "函数关系图",
                icon: "edit"
            },
            "cut": {
                name: "Cut",
                icon: "cut"
            },
            copy: {
                name: "Copy",
                icon: "copy"
            },
            "paste": {
                name: "Paste",
                icon: "paste"
            },
            "delete": {
                name: "Delete",
                icon: "delete"
            },
            "sep1": "---------",
            "quit": {
                name: "Quit",
                icon: function() {
                    return 'context-menu-icon context-menu-icon-quit';
                }
            }
        }
    });
})(jQuery);