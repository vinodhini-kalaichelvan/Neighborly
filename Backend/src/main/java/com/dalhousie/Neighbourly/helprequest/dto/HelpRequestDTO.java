package com.dalhousie.Neighbourly.helprequest.dto;

import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelpRequestDTO {
    private int requestId;
    private int userId;
    private int neighbourhoodId;
    private String requestType;
    private HelpRequest.RequestStatus status;
    private String description;

    public HelpRequestDTO buiHelpRequestDTO(HelpRequest helpRequest) {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setRequestId(requestId);
        dto.setUserId(helpRequest.getUser().getId());
        dto.setNeighbourhoodId(helpRequest.getNeighbourhood().getNeighbourhoodId());
        dto.setDescription(helpRequest.getDescription());
        dto.setStatus(helpRequest.getStatus());
        return dto;
    }
}
