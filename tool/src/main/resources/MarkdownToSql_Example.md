### Table Description(Table_Name)

| PK  | NULL | IDENTITY | 欄位       | 型態          | 說明                    | 代碼說明                 |
| --- | ---- | -------- | ---------- | ------------- | ----------------------- | ------------------------ |
| v   |      | v        | ColumnName     | Type           | Column Description  |  Content Sample                        |
|     |      |          | Type       | nvarchar(2)   | 1: 我方呼叫 2: 對方呼叫 |                          |
|     |      |          | KeyInfo    | nvarchar(200) | 識別此交易是由誰發出去  | 放 ULID 或其他查詢用資料 |
|     |      |          | ApiName    | nvarchar(200) | 呼叫的 API 名稱         |                          |
|     |      |          | Request    | nvarchar(max) | 呼叫 API 的需求電文     |                          |
|     |      |          | Response   | nvarchar(max) | API 的回應電文          |                          |
|     |      |          | StartTime  | datetime      | API 開始時間            |                          |
|     |      |          | EndTime    | datetime      | API 結束時間            |                          |
|     |      |          | CreateTime | datetime      | 新增時間                |                          |

### Talbe usage detail
