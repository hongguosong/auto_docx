(function ($) {
    'use strict';
    $(function () {
        //获取实时时间并更新
        var timeStr;
        function NowTime(){
            var time = new Date();
            //获取年月日
            var year = time.getFullYear();
            var month = time.getMonth() + 1; //月份是根据下标取的，所以需要加1
            var day = time.getDate();

            //获取时分秒
            var h = time.getHours();
            var m = time.getMinutes();
            var s = time.getSeconds();

            //时分秒填充0
            month = check(month);
            day = check(day);
            h = check(h);
            m = check(m);
            s = check(s);

            timeStr = year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s;
        }

        function check(hms) {
            if (hms < 10){
                hms = '0' + hms;
            }
            return hms;
        }

        if ($("#analyze-info").length) {
            $("#analyze-info").jsGrid({
                height: "400px",
                width: "100%",
                filtering: true,
                editing: true,
                //inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 15,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the client?",
                //data: db.userinfo,
                controller: {
                    loadData: function(filter) {
                        return $.grep(db.myfunction, function(client) {
                            return (!filter.Name || client.Name.indexOf(filter.Name) > -1)
                                && (!filter.VerificationCode || client.VerificationCode.indexOf(filter.VerificationCode > -1))
                                && (!filter.Vertion || client.Vertion.indexOf(filter.Vertion > -1))
                                && (!filter.Description || client.Description.indexOf(filter.Description > -1))
                                && (!filter.Output || client.Output.indexOf(filter.Output > -1))
                                && (!filter.Input || client.Input.indexOf(filter.Input > -1))
                                && (!filter.Performance || client.Performance.indexOf(filter.Performance > -1))
                                && (!filter.Feature || client.Feature.indexOf(filter.Feature > -1))
                                && (!filter.Creator || client.Creator === filter.Creator)
                                && (!filter.GlobalVariable || client.GlobalVariable.indexOf(filter.GlobalVariable > -1))
                                && (!filter.CreateTime || client.CreateTime.indexOf(filter.CreateTime > -1))
                                && (!filter.LastModifyTime || client.LastModifyTime.indexOf(filter.LastModifyTime > -1))
                                && (!filter.ParentId || client.ParentId === filter.ParentId);
                        });
                    },
                },
                fields: [
                    {
                        title: "标识",
                        name: "Id",
                        type: "number",
                        width: 50,
                        visible: false
                    },
                    {
                        title: "名称",
                        name: "Name",
                        type: "text",
                        align: "center",
                        width: 100,
                        validate: "required"
                    },
                    {
                        title: "验证码",
                        name: "VerificationCode",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [6, 80] }
                    },
                    {
                        title: "版本",
                        name: "Vertion",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [3, 10] }
                    },
                    {
                        title: "功能描述",
                        name: "Description",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "输出",
                        name: "Output",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "输入",
                        name: "Input",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "性能",
                        name: "Performance",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "特性",
                        name: "Feature",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "编写人",
                        name: "Creator",
                        type: "select",
                        items: db.userinfo,
                        valueField: "Id",
                        textField: "Name",
                        align: "center",
                        width: 100,
                        validate: { message: "Creator should be specified", validator: function(value) { return value >= 0; } }
                    },
                    {
                        title: "全局变量",
                        name: "GlobalVariable",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "录入日期",
                        name: "CreateTime",
                        type: "text",
                        align: "center",
                        width: 70
                    },
                    {
                        title: "更新日期",
                        name: "LastModifyTime",
                        type: "text",
                        align: "center",
                        width: 70
                    },
                    {
                        title: "父函数",
                        name: "ParentId",
                        type: "select",
                        items: db.myfunction,
                        valueField: "Id",
                        textField: "Name",
                        align: "center",
                        width: 100
                    },
                    {
                        title: "型号",
                        name: "Model",
                        type: "select",
                        // items: db.mymodel,
                        // valueField: "Id",
                        // textField: "Name",
                        align: "center",
                        width: 50
                    }
                    // ,
                    // {
                    //     type: "control"
                    // }
                ]
            });
        }

        if ($("#function-info").length) {
            $("#function-info").jsGrid({
                height: "450px",
                width: "100%",
                filtering: true,
                editing: true,
                //inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 15,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the client?",
                //data: db.userinfo,
                controller: {
                    loadData: function(filter) {
                        return $.grep(db.myfunction, function(client) {
                            return (!filter.Name || client.Name.indexOf(filter.Name) > -1)
                                && (!filter.VerificationCode || client.VerificationCode.indexOf(filter.VerificationCode > -1))
                                && (!filter.Vertion || client.Vertion.indexOf(filter.Vertion > -1))
                                && (!filter.Description || client.Description.indexOf(filter.Description > -1))
                                && (!filter.Output || client.Output.indexOf(filter.Output > -1))
                                && (!filter.Input || client.Input.indexOf(filter.Input > -1))
                                && (!filter.Performance || client.Performance.indexOf(filter.Performance > -1))
                                && (!filter.Feature || client.Feature.indexOf(filter.Feature > -1))
                                && (!filter.Creator || client.Creator === filter.Creator)
                                && (!filter.GlobalVariable || client.GlobalVariable.indexOf(filter.GlobalVariable > -1))
                                && (!filter.CreateTime || client.CreateTime.indexOf(filter.CreateTime > -1))
                                && (!filter.LastModifyTime || client.LastModifyTime.indexOf(filter.LastModifyTime > -1))
                                && (!filter.ParentId || client.ParentId === filter.ParentId);
                        });
                    },
                },
                fields: [
                    {
                        title: "标识",
                        name: "Id",
                        type: "number",
                        width: 50,
                        visible: false
                    },
                    {
                        title: "名称",
                        name: "Name",
                        type: "text",
                        align: "center",
                        width: 100,
                        validate: "required"
                    },
                    {
                        title: "验证码",
                        name: "VerificationCode",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [6, 80] }
                    },
                    {
                        title: "版本",
                        name: "Vertion",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [3, 10] }
                    },
                    {
                        title: "功能描述",
                        name: "Description",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "输出",
                        name: "Output",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "输入",
                        name: "Input",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "性能",
                        name: "Performance",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "特性",
                        name: "Feature",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "编写人",
                        name: "Creator",
                        type: "select",
                        items: db.userinfo,
                        valueField: "Id",
                        textField: "Name",
                        align: "center",
                        width: 100,
                        validate: { message: "Creator should be specified", validator: function(value) { return value >= 0; } }
                    },
                    {
                        title: "全局变量",
                        name: "GlobalVariable",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "录入日期",
                        name: "CreateTime",
                        type: "text",
                        align: "center",
                        width: 70
                    },
                    {
                        title: "更新日期",
                        name: "LastModifyTime",
                        type: "text",
                        align: "center",
                        width: 70
                    },
                    {
                        title: "父函数",
                        name: "ParentId",
                        type: "select",
                        items: db.myfunction,
                        valueField: "Id",
                        textField: "Name",
                        align: "center",
                        width: 100
                    },
                    {
                        title: "型号",
                        name: "Model",
                        type: "select",
                        // items: db.mymodel,
                        // valueField: "Id",
                        // textField: "Name",
                        align: "center",
                        width: 50
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        if ($("#function-history-info").length) {
            $("#function-history-info").jsGrid({
                height: "450px",
                width: "100%",
                filtering: true,
                editing: true,
                //inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 15,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the client?",
                //data: db.userinfo,
                controller: {
                    loadData: function(filter) {
                        return $.grep(db.myfunction, function(client) {
                            return (!filter.Name || client.Name.indexOf(filter.Name) > -1)
                                && (!filter.VerificationCode || client.VerificationCode.indexOf(filter.VerificationCode > -1))
                                && (!filter.Vertion || client.Vertion.indexOf(filter.Vertion > -1))
                                && (!filter.Description || client.Description.indexOf(filter.Description > -1))
                                && (!filter.Output || client.Output.indexOf(filter.Output > -1))
                                && (!filter.Input || client.Input.indexOf(filter.Input > -1))
                                && (!filter.Performance || client.Performance.indexOf(filter.Performance > -1))
                                && (!filter.Feature || client.Feature.indexOf(filter.Feature > -1))
                                && (!filter.Creator || client.Creator === filter.Creator)
                                && (!filter.GlobalVariable || client.GlobalVariable.indexOf(filter.GlobalVariable > -1))
                                && (!filter.CreateTime || client.CreateTime.indexOf(filter.CreateTime > -1))
                                && (!filter.LastModifyTime || client.LastModifyTime.indexOf(filter.LastModifyTime > -1))
                                && (!filter.ParentId || client.ParentId === filter.ParentId);
                        });
                    },
                },
                fields: [
                    {
                        title: "标识",
                        name: "Id",
                        type: "number",
                        width: 50,
                        visible: false
                    },
                    {
                        title: "名称",
                        name: "Name",
                        type: "text",
                        align: "center",
                        width: 100,
                        validate: "required"
                    },
                    {
                        title: "验证码",
                        name: "VerificationCode",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [6, 80] }
                    },
                    {
                        title: "版本",
                        name: "Vertion",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [3, 10] }
                    },
                    {
                        title: "功能描述",
                        name: "Description",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "输出",
                        name: "Output",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "输入",
                        name: "Input",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "性能",
                        name: "Performance",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "特性",
                        name: "Feature",
                        type: "text",
                        align: "center",
                        width: 50,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "编写人",
                        name: "Creator",
                        type: "select",
                        items: db.userinfo,
                        valueField: "Id",
                        textField: "Name",
                        align: "center",
                        width: 100,
                        validate: { message: "Creator should be specified", validator: function(value) { return value >= 0; } }
                    },
                    {
                        title: "全局变量",
                        name: "GlobalVariable",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "range", param: [1, 100] }
                    },
                    {
                        title: "录入日期",
                        name: "CreateTime",
                        type: "text",
                        align: "center",
                        width: 70
                    },
                    {
                        title: "更新日期",
                        name: "LastModifyTime",
                        type: "text",
                        align: "center",
                        width: 70
                    },
                    {
                        title: "父函数",
                        name: "ParentId",
                        type: "select",
                        items: db.myfunction,
                        valueField: "Id",
                        textField: "Name",
                        align: "center",
                        width: 100
                    },
                    {
                        title: "型号",
                        name: "Model",
                        type: "select",
                        // items: db.mymodel,
                        // valueField: "Id",
                        // textField: "Name",
                        align: "center",
                        width: 50
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        //basic config
        if ($("#user-info").length) {
            $("#user-info").jsGrid({
                height: "630px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageLoading: true,
                pageSize: 10,
                pageButtonCount: 5,
                deleteConfirm: "确定删除该用户?",
                controller: {
                    loadData: function(filter) {
                        return $.ajax({
                            type: "GET",
                            url: "/user/selectUsers",
                            data: {"pageIndex":filter.pageIndex - 1, "pageSize": filter.pageSize}
                        })
                    },
                    insertItem: function(insertingClient) {
                        return $.ajax({
                            type: "POST",
                            url: "/user/insertUser",
                            data: {
                                "username": insertingClient.username,
                                "nickName": insertingClient.nickName,
                                "email": insertingClient.email,
                                "createTime": insertingClient.createTime,
                                "updateTime": insertingClient.updateTime,
                                "roleId": insertingClient.roleId
                            }
                        });
                    },
                    updateItem: function(updatingClient) {
                        return $.ajax({
                            type: "PUT",
                            url: "/user/updateUser",
                            data: {
                                "id": updatingClient.id,
                                "username": updatingClient.username,
                                "nickName": updatingClient.nickName,
                                "email": updatingClient.email,
                                "createTime": updatingClient.createTime,
                                "updateTime": updatingClient.updateTime,
                                "roleId": updatingClient.roleId
                            }
                        })
                    },
                    deleteItem: function(deletingClient) {
                        return $.ajax({
                            type: "DELETE",
                            url: "/user/deleteUser",
                            data: {"id": deletingClient.id}
                        });
                    }
                },
                fields: [
                    {
                        title: "标识",
                        name: "id",
                        type: "text",
                        width: 50,
                        visible: false
                    },
                    {
                        title: "账号名称",
                        name: "username",
                        type: "text",
                        align: "center",
                        width: 100,
                        validate: "required"
                    },
                    {
                        title: "姓名",
                        name: "nickName",
                        type: "text",
                        align: "center",
                        width: 100,
                        validate: "required"
                    },
                    {
                        title: "邮件地址",
                        name: "email",
                        type: "text",
                        align: "center",
                        width: 150,
                        validate: { validator: "rangeLength", param: [6, 80] }
                    },
                    {
                        title: "角色",
                        name: "roleId",
                        type: "select",
                        items: db.role,
                        valueField: "Id",
                        textField: "Name",
                        align: "center",
                        width: 100,
                        validate: { message: "Role should be specified", validator: function(value) { return value >= 0; } }
                    },
                    {
                        title: "注册日期",
                        name: "createTime",
                        type: "text",
                        align: "center",
                        width: 100,
                        // inserting: false,
                        editing: false,
                        insertValue: function () {
                            //设置实时创建时间和更新时间
                            NowTime();
                            setInterval(NowTime, 1000);
                            return timeStr;
                        }
                    },
                    {
                        title: "更新日期",
                        name: "updateTime",
                        type: "text",
                        align: "center",
                        width: 100,
                        // inserting: false,
                        // editing: false,
                        insertValue: function () {
                            //设置实时创建时间和更新时间
                            NowTime();
                            setInterval(NowTime, 1000);
                            return timeStr;
                        },
                        editValue: function () {
                            //设置实时创建时间和更新时间
                            NowTime();
                            setInterval(NowTime, 1000);
                            return timeStr;
                        }
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        if ($("#group-info").length) {
            $("#group-info").jsGrid({
                height: "630px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 15,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the client?",
                //data: db.group,
                controller: {
                    loadData: function(filter) {
                        var group = db.group.slice(1, db.group.length)
                        return $.grep(group, function(client) {
                            return (!filter.Name || client.Name.indexOf(filter.Name) > -1)
                                && (!filter.Manager || client.Manager === filter.Manager)
                                && (!filter.ParentId || client.ParentId === filter.ParentId);
                        });
                    },
                    insertItem: function(insertingClient) {
                        db.group.push(insertingClient);
                    },
                    updateItem: function(updatingClient) {
                        for(var i=0; i<db.group.length; i++){
                            if(db.group[i].Id == updatingClient.Id){
                                db.group[i].Name = updatingClient.Name;
                                db.group[i].Manager = updatingClient.Manager;
                            }
                        }
                    },
                    deleteItem: function(deletingClient) {
                        var clientIndex = $.inArray(deletingClient, db.group);
                        db.group.splice(clientIndex, 1);
                    }
                },
                fields: [
                    {
                        title: "标识",
                        name: "Id",
                        type: "number",
                        width: 50,
                        visible: false
                    },
                    {
                        title: "名称",
                        name: "Name",
                        type: "text",
                        width: 150,
                        align: "center"
                    },
                    {
                        title: "负责人",
                        name: "Manager",
                        type: "select",
                        items: db.userinfo,
                        valueField: "Id",
                        textField: "Name",
                        width: 150,
                        align: "center"
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        if ($("#role-info").length) {
            $("#role-info").jsGrid({
                height: "630px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 15,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the client?",
                //data: db.role,
                controller: {
                    loadData: function(filter) {
                        var role = db.role.slice(1, db.role.length)
                        return $.grep(role, function(client) {
                            return (!filter.Name || client.Name.indexOf(filter.Name) > -1)
                                && (!filter.Descript || client.Descript.indexOf(filter.Descript > -1))
                                && (!filter.DataAccessLevel || client.DataAccessLevel === filter.DataAccessLevel);
                        });
                    },
                    insertItem: function(insertingClient) {
                        db.role.push(insertingClient);
                    },
                    updateItem: function(updatingClient) {
                        for(var i=0; i<db.role.length; i++){
                            if(db.role[i].Id == updatingClient.Id){
                                db.role[i].Name = updatingClient.Name;
                                db.role[i].Descript = updatingClient.Descript;
                                db.role[i].DataAccessLevel = updatingClient.DataAccessLevel;
                            }
                        }
                    },
                    deleteItem: function(deletingClient) {
                        var clientIndex = $.inArray(deletingClient, db.role);
                        db.role.splice(clientIndex, 1);
                    }
                },
                fields: [
                    {
                        title: "标识",
                        name: "Id",
                        type: "number",
                        width: 50,
                        visible: false
                    },
                    {
                        title: "名称",
                        name: "Name",
                        type: "text",
                        width: 150,
                        align: "center"
                    },
                    {
                        title: "描述",
                        name: "Descript",
                        type: "text",
                        width: 150,
                        align: "center"
                    },
                    {
                        title: "数据访问级别",
                        name: "DataAccessLevel",
                        type: "select",
                        items: db.dataaccesslevel,
                        valueField: "Id",
                        textField: "Name",
                        width: 150,
                        align: "center"
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        if ($("#permission-info").length) {
            $("#permission-info").jsGrid({
                height: "630px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageLoading:true,
                pagerFormat: "{first} {prev} {pages} {next} {last}",
                pagePrevText: "上一页",
                pageNextText: "下一页",
                pageFirstText: "首页",
                pageLastText: "尾页",
                pageSize: 10,
                pageButtonCount: 5,
                deleteConfirm: "确定删除该权限?",
                // data: db.permission,
                controller: {
                    loadData: function(filter) {
                        return $.ajax({
                            type: "GET",
                            url: "/permission/selectPermissions",
                            data: {"pageIndex":filter.pageIndex - 1, "pageSize": filter.pageSize}
                        })
                    },
                    insertItem: function(insertingClient) {
                        return $.ajax({
                            type: "POST",
                            url: "/permission/insertPermission",
                            data: {"id":insertingClient.id, "menuName":insertingClient.menuName, "url":insertingClient.url, "menuCode":insertingClient.menuCode}
                        });
                    },
                    updateItem: function(updatingClient) {
                        return $.ajax({
                            type: "PUT",
                            url: "/permission/updatePermission",
                            data: {"id":updatingClient.id, "menuName":updatingClient.menuName, "url":updatingClient.url, "menuCode":updatingClient.menuCode}
                        })
                    },
                    deleteItem: function(deletingClient) {
                        return $.ajax({
                            type: "DELETE",
                            url: "/permission/deletePermission",
                            data: {"id": deletingClient.id}
                        });
                    }
                },
                fields: [
                    {
                        title: "标识",
                        name: "id",
                        type: "text",
                        width: 50,
                        align: "center",
                        visible: false
                    },
                    {
                        title: "名称",
                        name: "menuName",
                        type: "text",
                        width: 150,
                        align: "center"
                    },
                    {
                        title: "URL",
                        name: "url",
                        type: "text",
                        width: 150,
                        align: "center"
                    },
                    {
                        title: "权限",
                        name: "menuCode",
                        type: "text",
                        width: 150,
                        align: "center"
                    },
                    {
                        title: "父标识",
                        name: "parentId",
                        type: "text",
                        width: 50,
                        visible: false
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

        if ($("#rolepermission-info").length) {
            $("#rolepermission-info").jsGrid({
                height: "630px",
                width: "100%",
                filtering: true,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 15,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the client?",
                //data: db.rolepermission,
                controller: {
                    loadData: function(filter) {
                        var rolepermission = db.rolepermission.slice(1, db.rolepermission.length)
                        return $.grep(rolepermission, function(client) {
                            return (!filter.RoleId || client.RoleId === filter.RoleId)
                                && (!filter.PermissionId || client.PermissionId === filter.PermissionId);
                        });
                    },
                    insertItem: function(insertingClient) {
                        db.rolepermission.push(insertingClient);
                    },
                    updateItem: function(updatingClient) {
                        for(var i=0; i<db.rolepermission.length; i++){
                            if(db.rolepermission[i].Id == updatingClient.Id){
                                db.rolepermission[i].RoleId = updatingClient.RoleId;
                                db.rolepermission[i].PermissionId = updatingClient.PermissionId;
                            }
                        }
                    },
                    deleteItem: function(deletingClient) {
                        var clientIndex = $.inArray(deletingClient, db.rolepermission);
                        db.rolepermission.splice(clientIndex, 1);
                    }
                },
                fields: [
                    {
                        title: "标识",
                        name: "Id",
                        type: "number",
                        width: 50,
                        visible: false
                    },
                    {
                        title: "角色名称",
                        name: "RoleId",
                        type: "select",
                        items: db.role,
                        valueField: "Id",
                        textField: "Name",
                        width: 150,
                        align: "center"
                    },
                    {
                        title: "权限名称",
                        name: "PermissionId",
                        type: "select",
                        items: db.permission,
                        valueField: "Id",
                        textField: "Name",
                        width: 150,
                        align: "center"
                    },
                    {
                        type: "control"
                    }
                ]
            });
        }

    });
})(jQuery);
