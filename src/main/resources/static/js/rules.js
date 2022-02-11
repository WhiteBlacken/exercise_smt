var rules = {
    data(){
        return {
            rules: {
                prob_text: [{
                    required: true,
                    message: '请输入题干内容',
                    trigger: 'blur'
                }, {
                    min: 2,
                    max: 400,
                    message: '长度在 2 到 400 个字符',
                    trigger: 'blur'
                }],
                answer: [{
                    required: true,
                    message: '答案不能为空',
                    trigger: 'blur'
                },],
            },
        }
    }
}