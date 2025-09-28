package com.ndh.ShopTechnology.controller.job;

import com.ndh.ShopTechnology.constant.MessageConstant;
import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.request.job.CreateJobReportRequest;
import com.ndh.ShopTechnology.dto.request.job.GetJobReportRequest;
import com.ndh.ShopTechnology.dto.request.job.ModJobReportRequest;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import com.ndh.ShopTechnology.entities.job.JobReportDetailEntity;
import com.ndh.ShopTechnology.entities.job.JobReportEntity;
import com.ndh.ShopTechnology.services.job.JobReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/job")
public class JobReportController {


    private final JobReportService jobReportService;

    @Autowired
    public JobReportController(JobReportService jobReportService) {
        this.jobReportService = jobReportService;
    }

    @PostMapping
    public ResponseEntity<APIResponse> createJobReport(@RequestBody CreateJobReportRequest request) throws Exception {
        JobReportEntity ent = jobReportService.createReportForUser(request);
        if (ent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.doResponse(
                            DefRes.STAT_CODE, DefRes.STATUS_NOT_FOUND,
                            DefRes.RES_DES, MessageConstant.USER_NOT_FOUND
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, ent
                ));
    }

    @PutMapping
    public ResponseEntity<APIResponse> modifyJobReport(@RequestBody ModJobReportRequest request) throws Exception {
        JobReportDetailEntity ent = jobReportService.modJobReportDetails(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, ent
                ));
    }

    @PostMapping("/details")
    public ResponseEntity<APIResponse> getJobReportForUser(@RequestBody GetJobReportRequest request) throws Exception {
        JobReportDetailEntity ent = jobReportService.getJobReportForUser(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, ent
                ));
    }

}
