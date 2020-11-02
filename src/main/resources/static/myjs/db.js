(function ($) {
    (function () {

        var db = {};

        window.db = db;

        db.userinfo = [{
                Name: "",
                Id: 0,
                Email: "",
                Group: 0,
                CreateTime: "",
                DeleteFlag: 0,
                Role: 0
            },
            {
                Name: "Admin",
                Id: 1,
                Email: "994662950@qq.com",
                Group: 1,
                CreateTime: "2019-10-16",
                DeleteFlag: 1,
                Role: 1
            },
            {
                Name: "userA",
                Id: 2,
                Email: "994662950@qq.com",
                Group: 1,
                CreateTime: "2019-10-16",
                DeleteFlag: 1,
                Role: 2
            },
            {
                Name: "userB",
                Id: 3,
                Email: "994662950@qq.com",
                Group: 1,
                CreateTime: "2019-10-16",
                DeleteFlag: 1,
                Role: 1
            }
        ];

        db.userdeleteflag = [
            {
                Name: "",
                Id: 0
            },
            {
                Name: "正常",
                Id: 1
            },
            {
                Name: "锁定",
                Id: 2
            },
            {
                Name: "注销",
                Id: 3
            }
        ]

        db.dataaccesslevel = [
            {
                Name: "",
                Id: 0
            },
            {
                Name: "所有",
                Id: 1
            },
            {
                Name: "所属组",
                Id: 2
            },
            {
                Name: "个人",
                Id: 3
            }
        ]

        db.group = [
            {
                Name: "",
                Id: 0,
                Manager: 1,
                ParentId: -1
            },
            {
                Name: "信息组",
                Id: 1,
                Manager: 1,
                ParentId: -1
            },
            {
                Name: "测试组",
                Id: 2,
                Manager: 2,
                ParentId: 0
            }
        ]

        db.role = [
            {
                Name: "",
                Id: 0,
                Descript: "",
                DataAccessLevel: 0
            },
            {
                Name: "admin",
                Id: 1,
                Descript: "管理员",
                DataAccessLevel: 1
            },
            {
                Name: "user",
                Id: 2,
                Descript: "普通用户",
                DataAccessLevel: 2
            }
        ]

        db.ordertype = [
            {
                Name: "",
                Id: 0
            },
            {
                Name: "目录",
                Id: 1
            },
            {
                Name: "菜单",
                Id: 2
            },
            {
                Name: "按钮",
                Id: 3
            }
        ]

        db.permission = [
            {
                Id: 0,
                Name: "",
                ParentId: -1,
                URL: "",
                Perms: "",
                Type: 1
            },
            {
                Id: 1,
                Name: "模块管理",
                ParentId: -1,
                URL: "",
                Perms: "function",
                Type: 2
            },
            {
                Id: 2,
                Name: "项目管理",
                ParentId: -1,
                URL: "",
                Perms: "project",
                Type: 2
            },
            {
                Id: 201,
                Name: "项目管理/添加",
                ParentId: 2,
                URL: "",
                Perms: "project:add",
                Type: 3
            },
            {
                Id: 3,
                Name: "问题单管理",
                ParentId: -1,
                URL: "",
                Perms: "promblem",
                Type: 2
            }
        ]

        db.rolepermission = [
            {
                Id: 0,
                RoleId: 0,
                PermissionId: 0
            },
            {
                Id: 1,
                RoleId: 2,
                PermissionId: 1
            },
            {
                Id: 2,
                RoleId: 2,
                PermissionId: 2
            },
            {
                Id: 3,
                RoleId: 2,
                PermissionId: 3
            }
        ]

        db.myfunction = [
            {
                Id: 0,
                Name: "",
                VerificationCode: "",
                Vertion: "",
                Modify: 0,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creater: 0,
                GlobalVariable: "",
                CreateTime: "",
                LastModifyTime: "",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 1,
                Name: "ReadCom",
                VerificationCode: "56g522",
                Vertion: "1.00009",
                Modify: 1,
                Description: "函数功能：从内总线模块中读取数据",
                Output: "返回参数：Real_Len ->实际读取的长度",
                Input: "入口参数：Module ->模块名 COM0..COMBK,COM100..COM112\n" +
                "Buffer_Addr ->需要读取的数据缓冲区在内存中地址\n" +
                "Byte_Len ->读取的数据字节长度",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "MODULE=BSP.ONCHIPBUS.COM2;\n" +
                "BUFFER_ADDR=100;\n" +
                "BYTE_LEN=2;\n",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 2,
                Name: "WriteCom",
                VerificationCode: "56g522",
                Vertion: "1.00109",
                Modify: 1,
                Description: "函数功能：向内总线模块中发送数据",
                Output: "返回参数：Real_Len ->实际读取的长度",
                Input: "入口参数：Module ->模块名 COM0..COMBK,COM100..COM112\n" +
                "Buffer_Addr ->需要读取的数据缓冲区在内存中地址\n" +
                "Byte_Len ->读取的数据字节长度",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "MODULE=BSP.ONCHIPBUS.COM2;\n" +
                "BUFFER_ADDR=100;\n" +
                "BYTE_LEN=2;\n",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 3,
                Name: "IsBusy",
                VerificationCode: "56g522",
                Vertion: "1.00029",
                Modify: 2,
                Description: "函数功能：获取串口忙闲状态",
                Output: "返回参数：忙闲状态 True忙 False 闲",
                Input: "入口参数：Module ->模块名 COM0..COM15",
                Performance: "",
                Feature: "",
                Creator: 2,
                GlobalVariable: "MODULE=BSP.ONCHIPBUS.COM2;\n" +
                "BUFFER_ADDR=100;\n" +
                "BYTE_LEN=2;\n",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 4,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 5,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 6,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 7,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 8,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            },
            {
                Id: 9,
                Name: "Init",
                VerificationCode: "56g522",
                Vertion: "1.00000",
                Modify: 3,
                Description: "",
                Output: "",
                Input: "",
                Performance: "",
                Feature: "",
                Creator: 1,
                GlobalVariable: "",
                CreateTime: "2013.12.25",
                LastModifyTime: "2015.05.11",
                ParentId: -1,
                Model: 1
            }
        ]

        db.problem = [
            {
                Id: 1,
                Name: "问题一号",
                Property: "ksdnfa"
            },
            {
                Id: 2,
                Name: "问题二号",
                Property: "afaasdfss"
            },
            {
                Id: 3,
                Name: "问题三号",
                Property: "aefeawffweg"
            },
            {
                Id: 4,
                Name: "问题四号",
                Property: "ujkuykyruk"
            }
        ];

        db.project = [{
            Id: 1,
            No: "456",
            CreateTime: "2018-10-01",
            Version: "1.0",
            Manager: 1901
        },
            {
                Id: 2,
                No: "358",
                CreateTime: "2018-10-02",
                Version: "2.0",
                Manager: 1902
            },
            {
                Id: 3,
                No: "654",
                CreateTime: "2018-10-03",
                Version: "3.0",
                Manager: 1903
            },
            {
                Id: 4,
                No: "159",
                CreateTime: "2018-10-04",
                Version: "4.0",
                Manager: 1904
            },
            {
                Id: 5,
                No: "354",
                CreateTime: "2018-10-05",
                Version: "4.1",
                Manager: 1905
            },
            {
                Id: 6,
                No: "425",
                CreateTime: "2018-10-06",
                Version: "4.2",
                Manager: 1906
            },
            {
                Id: 7,
                No: "353",
                CreateTime: "2018-10-07",
                Version: "4.3",
                Manager: 1907
            },
            {
                Id: 8,
                No: "367",
                CreateTime: "2018-10-08",
                Version: "4.4",
                Manager: 1908
            },
            {
                Id: 9,
                No: "343",
                CreateTime: "2018-10-09",
                Version: "4.5",
                Manager: 1909
            },
            {
                Id: 10,
                No: "333",
                CreateTime: "2018-10-10",
                Version: "4.6",
                Manager: 1910
            },
            {
                Id: 11,
                No: "224",
                CreateTime: "2018-10-11",
                Version: "4.7",
                Manager: 1911
            },
            {
                Id: 12,
                No: "636",
                CreateTime: "2018-10-12",
                Version: "4.8",
                Manager: 1912
            },
            {
                Id: 13,
                No: "144",
                CreateTime: "2018-10-13",
                Version: "4.9",
                Manager: 1913
            },
            {
                Id: 14,
                No: "786",
                CreateTime: "2018-10-14",
                Version: "4.10",
                Manager: 1914
            },
            {
                Id: 15,
                No: "453",
                CreateTime: "2018-10-15",
                Version: "4.12",
                Manager: 1915
            },
            {
                Id: 16,
                No: "894",
                CreateTime: "2018-10-16",
                Version: "4.13",
                Manager: 1916
            }
        ];

        db.testCase = [{
            Id: 1000,
            Name: "测试一号",
            CreateTime: "2018-10-01",
            LastModifyTime: "2019-10-10",
            Valid: 2
        },
            {
                Id: 1001,
                Name: "测试一号",
                CreateTime: "2018-10-02",
                LastModifyTime: "2019-10-11",
                Valid: 1
            },
            {
                Id: 1002,
                Name: "测试一号",
                CreateTime: "2018-10-03",
                LastModifyTime: "2019-10-12",
                Valid: 1
            },
            {
                Id: 1003,
                Name: "测试一号",
                CreateTime: "2018-10-04",
                LastModifyTime: "2019-10-13",
                Valid: 2
            }
        ];

        db.testCaseFlag = [
            {
                Name: "",
                Id: 0
            },
            {
                Name: "否",
                Id: 1
            },
            {
                Name: "是",
                Id: 2
            }
        ];

        db.clients = [
            {
                "Name": "Otto Clay",
                "Age": 61,
                "Country": 6,
                "Address": "Ap #897-1459 Quam Avenue"
            },
            {
                "Name": "Connor Johnston",
                "Age": 73,
                "Country": 7,
                "Address": "Ap #370-4647 Dis Av."
            },
            {
                "Name": "Lacey Hess",
                "Age": 29,
                "Country": 7,
                "Address": "Ap #365-8835 Integer St."
            },
            {
                "Name": "Timothy Henson",
                "Age": 78,
                "Country": 1,
                "Address": "911-5143 Luctus Ave"
            },
            {
                "Name": "Timothy Henson",
                "Age": 78,
                "Country": 1,
                "Address": "911-5143 Luctus Ave"
            },
            {
                "Name": "Ramona Benton",
                "Age": 43,
                "Country": 5,
                "Address": "Ap #614-689 Vehicula Street"
            },
            {
                "Name": "Ramona Benton",
                "Age": 43,
                "Country": 5,
                "Address": "Ap #614-689 Vehicula Street"
            },
            {
                "Name": "Timothy Henson",
                "Age": 78,
                "Country": 1,
                "Address": "911-5143 Luctus Ave"
            },
            {
                "Name": "Ramona Benton",
                "Age": 43,
                "Country": 5,
                "Address": "Ap #614-689 Vehicula Street"
            },
            {
                "Name": "Timothy Henson",
                "Age": 78,
                "Country": 1,
                "Address": "911-5143 Luctus Ave"
            },
            {
                "Name": "Ramona Benton",
                "Age": 43,
                "Country": 5,
                "Address": "Ap #614-689 Vehicula Street"
            },
            {
                "Name": "Ezra Tillman",
                "Age": 51,
                "Country": 1,
                "Address": "P.O. Box 738, 7583 Quisque St."
            }
        ];

        db.problemProperty = [
            {Id: 1, Name: "store", Label: "存储(页面标签)", PropertyName: "存储的大小(生成报告属性名称)"}
        ]

    }());
})(jQuery);
