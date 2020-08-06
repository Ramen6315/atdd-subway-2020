package wooteco.subway.maps.map.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;
import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.line.dto.LineResponse;
import wooteco.subway.maps.line.dto.LineStationResponse;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.maps.station.dto.StationResponse;

@WebMvcTest(controllers = {MapController.class})
public class PathDocumentation extends Documentation {
    @Autowired
    private MapController mapController;
    @MockBean
    private MapService mapService;

    protected TokenResponse tokenResponse;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token");
    }

    @Test
    void createMap() {
        StationResponse 잠실역 = new StationResponse(1L, "잠실역", LocalDateTime.now(), LocalDateTime.now());
        List<LineStationResponse> lineStationResponses = Lists.newArrayList(
                new LineStationResponse(잠실역,1L,3L,1,2)
        );
        List<LineResponse> lineResponses = Lists.newArrayList(
                new LineResponse(1L, "분당선","RED",LocalTime.now(), LocalTime.now(),1,lineStationResponses,LocalDateTime.now(), LocalDateTime.now())
        );
        MapResponse response = new MapResponse(lineResponses);
        when(mapService.findMap()).thenReturn(response);
        given().log().all().
                header("Authorization","Bearer" + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/maps").
                then().
                log().all().
                apply(document("maps/find-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        responseFields(
                                fieldWithPath("lineResponses[].id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("lineResponses[].name").type(JsonFieldType.STRING).description("lineResponse name"),
                                fieldWithPath("lineResponses[].color").type(JsonFieldType.STRING).description("lineResponse color"),
                                fieldWithPath("lineResponses[].startTime").type(JsonFieldType.STRING).description("lineResponse startTime"),
                                fieldWithPath("lineResponses[].endTime").type(JsonFieldType.STRING).description("lineResponse endTime"),
                                fieldWithPath("lineResponses[].intervalTime").type(JsonFieldType.NUMBER).description("lineResponse intervalTime"),
                                fieldWithPath("lineResponses[].stations[].station.id").type(JsonFieldType.NUMBER).description("lineResponse stations stations id"),
                                fieldWithPath("lineResponses[].stations[].station.name").type(JsonFieldType.STRING).description("lineResponse stations stations name"),
                                fieldWithPath("lineResponses[].stations[].preStationId").type(JsonFieldType.NUMBER).description("lineResponse stations stations preStationId"),
                                fieldWithPath("lineResponses[].stations[].lineId").type(JsonFieldType.NUMBER).description("lineResponse stations lineId"),
                                fieldWithPath("lineResponses[].stations[].distance").type(JsonFieldType.NUMBER).description("lineResponse stations distance"),
                                fieldWithPath("lineResponses[].stations[].duration").type(JsonFieldType.NUMBER).description("lineResponse stations duration"),
                                fieldWithPath("lineResponses[].createdDate").type(JsonFieldType.STRING).description("lineResponse createDate"),
                                fieldWithPath("lineResponses[].modifiedDate").type(JsonFieldType.STRING).description("lineResponse modifiedDate")
                                ))).
                extract();
    }

    @Test
    void createPaths() {
        List<StationResponse> stationResponses = Lists.newArrayList(
                new StationResponse(2L, "미금역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(5L, "판교역", LocalDateTime.now(), LocalDateTime.now())
        );
        PathResponse pathResponse = new PathResponse(stationResponses,3,4,100);
        when(mapService.findPath(2L,5L, PathType.DURATION)).thenReturn(pathResponse);
        given().log().all().
                header("Authorization", "Bearer"+tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={source}&target={target}&type={type}",2L,5L,PathType.DURATION).
                then().
                log().all().
                apply(document("paths/find-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        responseFields(
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역들의 id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역들의 이름"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("이동 시간"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("이동 거리"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
                        ))).
                extract();
    }
}
