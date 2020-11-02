(function ($) {
    'use strict';
    $(function () {

        if ($("#problem-grid").length) {
            $("#problem-grid").jsGrid({
                height: "500px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 10,
                pageButtonCount: 5,
                pagerFormat: "{first} {prev} {pages} {next} {last} 当前页为第 {pageIndex} 页,共 {pageCount} 页",
                pagePrevText: "上一页",
                pageNextText: "下一页",
                pageFirstText: "第一页",
                pageLastText: "最后一页",
                deleteConfirm: "是否删除该项目?",
                invalidMessage: "未输入有效数据!",
                noDataContent: "未查询到数据",
                loadMessage: "数据载入中...",
                controller: {
                    loadData: function(filter) {
                        return $.grep(db.problem, function(info) {
                            return (!filter.Id || info.Id === filter.Id)
                                && (!filter.Name || info.Name.indexOf(filter.Name) > -1)
                                && (!filter.Property || info.Property.indexOf(filter.Property) > -1);
                        });
                    }
                },
                fields: [
                    {
                        title: "标识",
                        align: "center",
                        name: "Id",
                        type: "number",
                        width: 50,
                        validate: {
                            validator: "required",
                            message: "标识为必填项"
                        }
                    },
                    {
                        title: "名称",
                        align: "center",
                        name: "Name",
                        type: "text",
                        width: 100,
                        validate: {
                            validator: "required",
                            message: "名称为必填项"
                        }
                    },
                    {
                        title: "属性",
                        align: "center",
                        name: "Property",
                        type: "text",
                        width: 100
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        if ($("#project-grid").length) {
            $("#project-grid").jsGrid({
                height: "500px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 10,
                pageButtonCount: 5,
                pagerFormat: "{first} {prev} {pages} {next} {last} 当前页为第 {pageIndex} 页,共 {pageCount} 页",
                pagePrevText: "上一页",
                pageNextText: "下一页",
                pageFirstText: "第一页",
                pageLastText: "最后一页",
                deleteConfirm: "是否删除该项目?",
                invalidMessage: "未输入有效数据!",
                noDataContent: "未查询到数据",
                loadMessage: "数据载入中...",
                controller: {
                    loadData: function(filter) {
                        return $.grep(db.project, function(info) {
                            return (!filter.Id || info.Id === filter.Id)
                                && (!filter.No || info.No.indexOf(filter.No) > -1)
                                && (!filter.CreateTime || info.CreateTime.indexOf(filter.CreateTime) > -1)
                                && (!filter.Version || info.Version.indexOf(filter.Version) > -1)
                                && (!filter.Manager || info.Manager === filter.Manager);
                        });
                    }
                },
                fields: [
                    {
                        title: "标识",
                        align: "center",
                        name: "Id",
                        type: "number",
                        width: 50,
                        validate: {
                            validator: "required",
                            message: "标识为必填项"
                        }
                    },
                    {
                        title: "代号",
                        align: "center",
                        name: "No",
                        type: "text",
                        width: 100,
                        validate: {
                            validator: "required",
                            message: "代号为必填项"
                        }
                    },
                    {
                        title: "日期",
                        align: "center",
                        name: "CreateTime",
                        type: "text",
                        width: 100,
                        validate: {
                            validator: "required",
                            message: "日期为必填项"
                        }
                    },
                    {
                        title: "版本号",
                        align: "center",
                        name: "Version",
                        type: "text",
                        width: 100
                    },
                    {
                        title: "负责人",
                        align: "center",
                        name: "Manager",
                        type: "number",
                        width: 100,
                        validate: {
                            validator: "required",
                            message: "负责人为必填项"
                        }
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        if ($("#testCase-grid").length) {
            $("#testCase-grid").jsGrid({
                height: "500px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 10,
                pageButtonCount: 5,
                pagerFormat: "{first} {prev} {pages} {next} {last} 当前页为第 {pageIndex} 页,共 {pageCount} 页",
                pagePrevText: "上一页",
                pageNextText: "下一页",
                pageFirstText: "第一页",
                pageLastText: "最后一页",
                deleteConfirm: "是否删除该项目?",
                invalidMessage: "未输入有效数据!",
                noDataContent: "未查询到数据",
                loadMessage: "数据载入中...",
                controller: {
                    loadData: function(filter) {
                        return $.grep(db.testCase, function(info) {
                            return (!filter.Id || info.Id === filter.Id)
                                && (!filter.Name || info.Name.indexOf(filter.Name) > -1)
                                && (!filter.CreateTime || info.CreateTime.indexOf(filter.CreateTime) > -1)
                                && (!filter.LastModifyTime || info.LastModifyTime.indexOf(filter.LastModifyTime) > -1)
                                && (!filter.Valid || info.Valid === filter.Valid);
                        });
                    }
                },
                fields: [
                    {
                        title: "标识",
                        align: "center",
                        name: "Id",
                        type: "number",
                        width: 50,
                        validate: {
                            validator: "required",
                            message: "标识为必填项"
                        }
                    },
                    {
                        title: "名称",
                        align: "center",
                        name: "Name",
                        type: "text",
                        width: 100,
                        validate: {
                            validator: "required",
                            message: "名称为必填项"
                        }
                    },
                    {
                        title: "录入日期",
                        align: "center",
                        name: "CreateTime",
                        type: "text",
                        width: 100,
                        validate: {
                            validator: "required",
                            message: "录入日期为必填项"
                        }
                    },
                    {
                        title: "更新日期",
                        align: "center",
                        name: "LastModifyTime",
                        type: "text",
                        width: 100,
                        validate: {
                            validator: "required",
                            message: "更新日期为必填项"
                        }
                    },
                    {
                        title: "是否有效",
                        align: "center",
                        name: "Valid",
                        type: "select",
                        items: db.testCaseFlag,
                        valueField: "Id",
                        textField: "Name",
                        width: 100,
                        validate: { message: "必须指定是否有效", validator: function(value) { return value > 0; } }
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

    });
})(jQuery);