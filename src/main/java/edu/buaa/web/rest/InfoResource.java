package edu.buaa.web.rest;
import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Constants;
import edu.buaa.domain.Info;
import edu.buaa.service.Constant;
import edu.buaa.service.InfoService;
import edu.buaa.web.rest.errors.BadRequestAlertException;
import edu.buaa.web.rest.util.FileUtils;
import edu.buaa.web.rest.util.HeaderUtil;
import edu.buaa.web.rest.util.ImageUtil;
import edu.buaa.web.rest.util.PaginationUtil;
import edu.buaa.service.dto.InfoCriteria;
import edu.buaa.service.InfoQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Info.
 */
@RestController
@RequestMapping("/api")
public class InfoResource {

    private final Logger log = LoggerFactory.getLogger(InfoResource.class);

    private static final String ENTITY_NAME = "edgeInfo";

    private final InfoService infoService;

    private final InfoQueryService infoQueryService;

    public InfoResource(InfoService infoService, InfoQueryService infoQueryService) {
        this.infoService = infoService;
        this.infoQueryService = infoQueryService;
    }

    /**
     * POST  /infos : Create a new info.
     *
     * @param info the info to create
     * @return the ResponseEntity with status 201 (Created) and with body the new info, or with status 400 (Bad Request) if the info has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/infos")
    public ResponseEntity<Info> createInfo(@Valid @RequestBody Info info) throws URISyntaxException {
        log.debug("REST request to save Info : {}", info);
        if (info.getId() != null) {
            throw new BadRequestAlertException("A new info cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Info result = infoService.save(info);
        return ResponseEntity.created(new URI("/api/infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /infos : Updates an existing info.
     *
     * @param info the info to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated info,
     * or with status 400 (Bad Request) if the info is not valid,
     * or with status 500 (Internal Server Error) if the info couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/infos")
    public ResponseEntity<Info> updateInfo(@Valid @RequestBody Info info) throws URISyntaxException {
        log.debug("REST request to update Info : {}", info);
        if (info.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Info result = infoService.save(info);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, info.getId().toString()))
            .body(result);
    }

    /**
     * GET  /infos : get all the infos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of infos in body
     */
    @GetMapping("/infos")
    public ResponseEntity<List<Info>> getAllInfos(InfoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Infos by criteria: {}", criteria);
        Page<Info> page = infoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/infos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /infos/count : count all the infos.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/infos/count")
    public ResponseEntity<Long> countInfos(InfoCriteria criteria) {
        log.debug("REST request to count Infos by criteria: {}", criteria);
        return ResponseEntity.ok().body(infoQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /infos/:id : get the "id" info.
     *
     * @param id the id of the info to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the info, or with status 404 (Not Found)
     */
    @GetMapping("/infos/{id}")
    public ResponseEntity<Info> getInfo(@PathVariable Long id) {
        log.debug("REST request to get Info : {}", id);
        Optional<Info> info = infoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(info);
    }

    /**
     * DELETE  /infos/:id : delete the "id" info.
     *
     * @param id the id of the info to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/infos/{id}")
    public ResponseEntity<Void> deleteInfo(@PathVariable Long id) {
        log.debug("REST request to delete Info : {}", id);
        infoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/infos/reset")
    public ResponseEntity<Info> resetall() {
        log.debug("REST request to reset Info ");
        infoService.deleteall();
        String path = Constants.filepathtosaveimg + File.separator + Constants.Edgename;
        FileUtils.delZSPic(path);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/importimage", consumes = "multipart/form-data")
    public ResponseEntity<String> importTopology (MultipartHttpServletRequest request){
        log.debug("REST request to upload files");

        MultipartFile multipartFile = request.getFile("uploadfile");
        //判断文件是否为空
        if (multipartFile == null) {
            log.error("null multipart");
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }
        //获取文件名
        String name = multipartFile.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size = multipartFile.getSize();
        if (name == null || ("").equals(name) || size == 0) {
            log.error("name error multipart");
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        String path = Constants.filepathtosaveimg + File.separator + Constants.Edgename;
        File file = new  File ( path );
        String filename = multipartFile.getOriginalFilename();
        String  pathFile = path + File.separator + filename;
        File  newFile = new  File(pathFile);
        //判断文件夹是否存在，不存在则创建
        if( !file.exists( ) ){
            //创建文件夹
            file.mkdirs();
        }
        try{
            //文件传输到本地
            multipartFile.transferTo(newFile);

        }catch(IOException e){
            e.printStackTrace();
        }

        // 文件流写入数据库表
        FileInputStream in = null;
        try
        {
            in = ImageUtil.readImage(pathFile);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            Info info = new Info();
            info.setFile_name(filename.split("\\.")[0]);
            info.setFile_type("png");
            info.setFile_size(newFile.length());
            info.setFile_body(bytes);
            infoService.save(info);
        } catch (Exception e) {
            log.debug("error write into mysql");
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }



//    @PostMapping(value = "/PostFileforimg")
//    public ResponseEntity<JSONObject> postFileforimg(@RequestParam("uploadfile") MultipartFile files) throws Exception {
//
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
