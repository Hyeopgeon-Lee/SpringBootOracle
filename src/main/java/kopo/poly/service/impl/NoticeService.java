package kopo.poly.service.impl;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.persistance.mapper.INoticeMapper;
import kopo.poly.service.INoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // noticeMapper 변수에 이미 메모리에 올라간 INoticeMapper 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final INoticeMapper noticeMapper;

    @Override
    public List<NoticeDTO> getNoticeList() throws Exception {

        log.info(this.getClass().getName() + ".getNoticeList start!");

        return noticeMapper.getNoticeList();

    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getNoticeInfo start!");

        // 상세보기할 때마다, 조회수 증가하기(수정보기는 제외)
        if (type) {
            log.info("Update ReadCNT");
            noticeMapper.updateNoticeReadCnt(pDTO);
        }

        return noticeMapper.getNoticeInfo(pDTO);

    }

    @Transactional
    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".InsertNoticeInfo start!");

        noticeMapper.insertNoticeInfo(pDTO);
    }

    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateNoticeInfo start!");

        noticeMapper.updateNoticeInfo(pDTO);

    }

    @Transactional
    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteNoticeInfo start!");

        noticeMapper.deleteNoticeInfo(pDTO);

    }
}
