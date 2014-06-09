#################### 
GROUP_NUMBER := 14
####################



ARCHIVE_NAME :=  OSM_2014_group_14_final_deliverable__$(shell date "+%Y-%m-%d__%H,%M,%S")__.tar.gz
ARCHIVE_DIR := ..


archive:
	cd $(ARCHIVE_DIR) && tar cvfz $(ARCHIVE_NAME) Blobswarm
	@echo
	@echo NOTE: Archive created in $(shell pwd)/$(ARCHIVE_NAME)
	@echo
