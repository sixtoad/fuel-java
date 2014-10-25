//
// This file is part of the Fuel Java SDK.
//
// Copyright (C) 2013, 2014 ExactTarget, Inc.
// All rights reserved.
//
// Permission is hereby granted, free of charge, to any person
// obtaining a copy of this software and associated documentation
// files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify,
// merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software
// is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
// KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
// OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
// OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
// OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

package com.exacttarget.fuelsdk.audiencebuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.exacttarget.fuelsdk.ETClient;
import com.exacttarget.fuelsdk.ETFilter;
import com.exacttarget.fuelsdk.ETFilter.AudienceBuilderFilter;
import com.exacttarget.fuelsdk.ETRestConnection;
import com.exacttarget.fuelsdk.ETRestObject;
import com.exacttarget.fuelsdk.ETSdkException;
import com.exacttarget.fuelsdk.annotations.ExternalName;
import com.exacttarget.fuelsdk.annotations.RestObject;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@RestObject(path = "/internal/v1/AudienceBuilder/Audience/{id}",
            primaryKey = "id",
            collectionKey = "entities")
public class ETAudience extends ETRestObject {
    @Expose @SerializedName("audienceDefinitionID")
    @ExternalName("id")
    private String id = null;
    @Expose
    @ExternalName("name")
    private String name = null;
    @Expose
    @ExternalName("description")
    private String description = null;
    @Expose
    @ExternalName("audienceCode")
    private String audienceCode = null;
    @Expose
    @ExternalName("publishCount")
    private Integer publishCount = null;
    @Expose
    @ExternalName("publishCountDate")
    private Date publishCountDate = null;
    @Expose
    private Filter filter = null;
    @ExternalName("filter")
    private ETFilter parsedFilter = null;           // internal
    @Expose
    @ExternalName("segments")
    private List<ETSegment> segments = new ArrayList<ETSegment>();

    private AudienceBuild audienceBuild = null;     // internal
    @Expose
    private List<AudienceBuild> audienceBuilds = new ArrayList<AudienceBuild>();

    private PublishResponse publishResponse = null; // internal

    public ETAudience() {
        //
        // By default we assume just one AudienceBuild:
        //

        audienceBuild = new AudienceBuild();
        audienceBuilds.add(audienceBuild);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudienceCode() {
        return audienceCode;
    }

    public void setAudienceCode(String audienceCode) {
        this.audienceCode = audienceCode;
    }

    public Integer getPublishCount() {
        return publishCount;
    }

    public Date getPublishCountDate() {
        return publishCountDate;
    }

    public String getStatus() {
        return publishResponse.getStatus();
    }

    public Integer getSubscribersCopied() {
        return publishResponse.getSubscribersCopied();
    }

    public ETFilter getFilter() {
        return parsedFilter;
    }

    public void setFilter(ETFilter filter) {
        parsedFilter = filter;
        this.filter = new Filter();
        this.filter.setFilterDefinition(parsedFilter.toAudienceBuilderFilter());
    }

    public void setFilter(String filter)
        throws ETSdkException
    {
        setFilter(ETFilter.parse(filter));
    }

    public List<ETSegment> getSegments() {
        return segments;
    }

    public void addSegment(ETSegment segment) {
        segments.add(segment);
    }

    public void addSegment(String filter)
        throws ETSdkException
    {
        ETSegment segment = new ETSegment();
        segment.setFilter(filter);
        segments.add(segment);
    }

    public String getPublishedDataExtensionName() {
        return audienceBuild.getDataExtensionName();
    }

    public void setPublishedDataExtensionName(String name) {
        audienceBuild.setDataExtensionName(name);
    }

    public Integer getPublishedDataExtensionFolderId() {
        return audienceBuild.getDataExtensionFolderId();
    }

    public void setPublishedDataExtensionFolderId(Integer folderId) {
        audienceBuild.setDataExtensionFolderId(folderId);
    }

    public static Integer retrieveAudienceCount(ETClient client, String filter)
        throws ETSdkException
    {
        ETAudience audience = new ETAudience();
        AudienceCountsRequest request = audience.new AudienceCountsRequest();
        ETFilter parsedFilter = ETFilter.parse(filter);
        request.addFilterDefinition(parsedFilter.toAudienceBuilderFilter());
        ETRestConnection connection = client.getRestConnection();
        Gson gson = new Gson();
        String json = connection.post("/internal/v1/AudienceBuilder/AudienceCounts",
                                      gson.toJson(request));
        AudienceCountsResponse response = gson.fromJson(json, AudienceCountsResponse.class);
        return response.getCount();
    }

    public void publish()
        throws ETSdkException
    {
        PublishRequest request = new PublishRequest();
        request.setAudienceDefinitionId(id);
        ETRestConnection connection = getClient().getRestConnection();
        Gson gson = new Gson();
        String json = connection.post("/internal/v1/AudienceBuilder/Publish",
                                      gson.toJson(request));
        publishResponse = gson.fromJson(json, PublishResponse.class);
    }

    public void updatePublishStatus()
        throws ETSdkException
    {
        ETRestConnection connection = getClient().getRestConnection();
        Gson gson = new Gson();
        String json = connection.get("/internal/v1/AudienceBuilder/Publish/" + id);
        publishResponse = gson.fromJson(json, PublishResponse.class);
    }

    //
    // These are just here so we can construct the JSON requests:
    //

    protected class AudienceBuild {
        @Expose
        private String name = "default";
        @Expose
        @SerializedName("publishedDataExtensionName")
        private String dataExtensionName = null;
        @Expose
        @SerializedName("publishedFolderCategoryID")
        private Integer dataExtensionFolderId = null;
        @Expose
        private Boolean available = true;
        @Expose
        private String publishChannel = "EMAIL";
        @Expose
        private String status = "";

        public String getDataExtensionName() {
            return dataExtensionName;
        }

        public void setDataExtensionName(String dataExtensionName) {
            this.dataExtensionName = dataExtensionName;
        }

        public Integer getDataExtensionFolderId() {
            return dataExtensionFolderId;
        }

        public void setDataExtensionFolderId(Integer dataExtensionFolderId) {
            this.dataExtensionFolderId = dataExtensionFolderId;
        }
    }

    protected class AudienceCountsRequest {
        @Expose
        @SerializedName("FilterDefinitions")
        private List<AudienceBuilderFilter> filterDefinitions = new ArrayList<AudienceBuilderFilter>();

        public void addFilterDefinition(AudienceBuilderFilter audienceBuilderFilter) {
            filterDefinitions.add(audienceBuilderFilter);
        }
    }

    protected class AudienceCountsResponse {
        @Expose
        private Integer count = null;

        public Integer getCount() {
            return count;
        }
    }

    protected class Filter {
        @Expose
        @SerializedName("filterDefinitionJSON")
        private AudienceBuilderFilter filterDefinition = null;

        public void setFilterDefinition(AudienceBuilderFilter filterDefinition) {
            this.filterDefinition = filterDefinition;
        }
    }

    protected class PublishRequest {
        @Expose
        @SerializedName("audienceBuilderPublishID")
        private String id = null;
        @Expose
        @SerializedName("audienceDefinitionID")
        private String audienceDefinitionId = null;

        public void setId(String id) {
            this.id = id;
        }

        public void setAudienceDefinitionId(String audienceDefinitionId) {
            this.audienceDefinitionId = audienceDefinitionId;
        }
    }

    protected class PublishResponse {
        @Expose
        @SerializedName("audienceBuilderPublishID")
        private String id = null;
        @Expose
        private String status = null;
        @Expose
        private Integer subscribersCopied = null;

        public String getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public Integer getSubscribersCopied() {
            return subscribersCopied;
        }
    }
}