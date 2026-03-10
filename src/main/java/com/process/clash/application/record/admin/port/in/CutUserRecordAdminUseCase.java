package com.process.clash.application.record.admin.port.in;

import com.process.clash.application.record.admin.data.CutUserRecordAdminData;

public interface CutUserRecordAdminUseCase {

    CutUserRecordAdminData.Result execute(CutUserRecordAdminData.Command command);
}