SUMMARY = "Loader to switch from aarch32 to aarch64 and boot"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e8c1458438ead3c34974bc0be3a03ed6"

DEPENDS = "edk2"

inherit deploy pythonnative

PV = "0.2.2"

SRC_URI = "git://github.com/96boards/l-loader.git;branch=tag-merge \
           file://temp.bin \
          "
SRCREV = "e1cbde79063e7db3f7171cdc9068d097056936ae"
S = "${WORKDIR}/git"

do_configure() {
	cp ${WORKDIR}/temp.bin ${S}/temp
	cp ${DEPLOY_DIR_IMAGE}/bl1.bin ${S}
}

do_compile() {
	python gen_loader.py -o l-loader.bin --img_loader=temp --img_bl1=bl1.bin
}

do_install() {
    install -d ${D}/boot
    install -m 0644 l-loader.bin ${D}/boot/
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 l-loader.bin ${DEPLOYDIR}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN} += "/boot"

addtask deploy before do_build after do_compile
